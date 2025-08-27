package LLD_ATM;// File: ATMExample.java
import java.util.*;

// ======================= Domain Enums =======================
enum DenominationType { HUNDRED, FIFTY, TEN }
enum OperationType { WITHDRAW, BALANCE_ENQUIRY, CHANGE_PIN }

// ======================= Entities =======================
class Account {
    private final String accountId;
    private int balance; // cents for production, integers for demo

    public Account(String accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }
    public String getAccountId() { return accountId; }
    public int getBalance() { return balance; }
    public void debit(int amount) { this.balance -= amount; }
    public void credit(int amount) { this.balance += amount; }
}

class Card {
    private final int cardNumber;
    private final String accountId;
    private final int pinHash; // demo only

    public Card(int cardNumber, String accountId, int pin) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.pinHash = Objects.hash(pin);
    }
    public int getCardNumber() { return cardNumber; }
    public String getAccountId() { return accountId; }
    public boolean validatePin(int pin) { return Objects.hash(pin) == pinHash; }
}

// ======================= Repositories =======================
class AccountManager {
    private final Map<String, Account> byId = new HashMap<>();
    public void add(Account a) { byId.put(a.getAccountId(), a); }
    public Account getById(String id) { return byId.get(id); }
}

class CardManager {
    private final Map<Integer, Card> byCard = new HashMap<>();
    public void add(Card c) { byCard.put(c.getCardNumber(), c); }
    public Card getByNumber(int cardNumber) { return byCard.get(cardNumber); }
}

// ======================= Cash Inventory =======================
class CashInventory {
    // value -> count
    private final Map<Integer, Integer> notes = new TreeMap<>(Comparator.reverseOrder()); // 100, 50, 10 high to low

    public CashInventory() {
        notes.put(100, 0);
        notes.put(50, 0);
        notes.put(10, 0);
    }

    public void load(int value, int count) {
        notes.compute(value, (k, v) -> (v == null ? 0 : v) + count);
    }

    public int totalAmount() {
        int total = 0;
        for (Map.Entry<Integer, Integer> e : notes.entrySet()) total += e.getKey() * e.getValue();
        return total;
    }

    public int getCount(int value) { return notes.getOrDefault(value, 0); }
    public void deduct(int value, int count) { notes.put(value, getCount(value) - count); }

    public Map<Integer, Integer> snapshot() { return new TreeMap<>(notes); }
}

// ======================= Cash Dispenser Chain =======================
interface Dispenser {
    void setNext(Dispenser next);
    // tries to fulfill part of amount using this node and passes remainder
    Map<Integer, Integer> dispense(int amount, CashInventory inventory);
}

abstract class BaseDispenser implements Dispenser {
    protected Dispenser next;
    protected final int value; // 100, 50, 10

    protected BaseDispenser(int value) { this.value = value; }

    public void setNext(Dispenser next) { this.next = next; }

    public Map<Integer, Integer> dispense(int amount, CashInventory inv) {
        Map<Integer, Integer> result = new HashMap<>();
        int needed = amount / value;
        int use = Math.min(needed, inv.getCount(value));
        if (use > 0) result.put(value, use);
        int remainder = amount - use * value;
        if (remainder == 0) return result;
        if (next == null) return null; // cannot fulfill
        Map<Integer, Integer> tail = next.dispense(remainder, inv);
        if (tail == null) return null;
        result.putAll(tail);
        return result;
    }
}

class HundredDispenser extends BaseDispenser { public HundredDispenser() { super(100); } }
class FiftyDispenser   extends BaseDispenser { public FiftyDispenser()   { super(50); } }
class TenDispenser     extends BaseDispenser { public TenDispenser()     { super(10); } }

class DispenserChain {
    private final Dispenser head;

    public DispenserChain() {
        Dispenser d100 = new HundredDispenser();
        Dispenser d50  = new FiftyDispenser();
        Dispenser d10  = new TenDispenser();
        d100.setNext(d50);
        d50.setNext(d10);
        head = d100;
    }

    public boolean canDispense(int amount, CashInventory inventory) {
        if (amount % 10 != 0) return false;
        Map<Integer, Integer> plan = head.dispense(amount, inventory);
        return plan != null;
    }

    public Map<Integer, Integer> dispenseAndDeduct(int amount, CashInventory inventory) {
        Map<Integer, Integer> plan = head.dispense(amount, inventory);
        if (plan == null) return null;
        for (Map.Entry<Integer, Integer> e : plan.entrySet()) inventory.deduct(e.getKey(), e.getValue());
        return plan;
    }
}

// ======================= Operations =======================
interface Performable {
    void perform();
}

class WithdrawMoney implements Performable {
    private final ATMContext ctx;
    private final int amount;

    public WithdrawMoney(ATMContext ctx, int amount) {
        this.ctx = ctx;
        this.amount = amount;
    }

    public void perform() {
        if (ctx.currentCard == null) throw new IllegalStateException("No card in session");
        Account acc = ctx.accountManager.getById(ctx.currentCard.getAccountId());
        if (amount <= 0 || amount % 10 != 0) throw new IllegalArgumentException("Amount must be positive multiple of 10");
        if (acc.getBalance() < amount) throw new IllegalStateException("Insufficient account balance");
        if (!ctx.dispenserChain.canDispense(amount, ctx.cashInventory)) throw new IllegalStateException("ATM cannot dispense requested amount");

        // Deduct from inventory then from account
        Map<Integer, Integer> bundle = ctx.dispenserChain.dispenseAndDeduct(amount, ctx.cashInventory);
        if (bundle == null) throw new IllegalStateException("Dispense plan failed");

        acc.debit(amount);
        ctx.audit("WITHDRAW", "amount=" + amount + ", notes=" + bundle);
    }
}

class BalanceEnquiry implements Performable {
    private final ATMContext ctx;
    public BalanceEnquiry(ATMContext ctx) { this.ctx = ctx; }
    public void perform() {
        Account acc = ctx.accountManager.getById(ctx.currentCard.getAccountId());
        ctx.audit("BALANCE_ENQUIRY", "balance=" + acc.getBalance());
    }
}

class ChangePin implements Performable {
    private final ATMContext ctx;
    private final int oldPin;
    private final int newPin;

    public ChangePin(ATMContext ctx, int oldPin, int newPin) {
        this.ctx = ctx; this.oldPin = oldPin; this.newPin = newPin;
    }

    public void perform() {
        // demo only, real systems would update secure store
        if (!ctx.currentCard.validatePin(oldPin)) throw new IllegalStateException("Old pin incorrect");
        // cannot actually mutate hashed pin on Card in this demo, but we would in a real repo
        ctx.audit("CHANGE_PIN", "pin_changed=true");
    }
}

class OperationFactory {
    public Performable getOperation(OperationType type, ATMContext ctx, Map<String, Object> params) {
        return switch (type) {
            case WITHDRAW -> new WithdrawMoney(ctx, (Integer) params.getOrDefault("amount", 0));
            case BALANCE_ENQUIRY -> new BalanceEnquiry(ctx);
            case CHANGE_PIN -> new ChangePin(ctx,
                    (Integer) params.getOrDefault("oldPin", 0),
                    (Integer) params.getOrDefault("newPin", 0));
        };
    }
}

// ======================= State Machine =======================
interface State {
    void insertCard(ATMContext ctx, int cardNumber);
    void enterPin(ATMContext ctx, int pin);
    void selectOperation(ATMContext ctx, OperationType type, Map<String, Object> params);
    void performOperation(ATMContext ctx);
    void ejectCard(ATMContext ctx);
}

class IdleState implements State {
    public void insertCard(ATMContext ctx, int cardNumber) {
        Card c = ctx.cardManager.getByNumber(cardNumber);
        if (c == null) throw new IllegalArgumentException("Unknown card");
        ctx.currentCard = c;
        ctx.setState(new HasCardState());
        ctx.audit("CARD_INSERTED", "card=" + cardNumber);
    }
    public void enterPin(ATMContext ctx, int pin) { ctx.disallow("enterPin"); }
    public void selectOperation(ATMContext ctx, OperationType t, Map<String, Object> p) { ctx.disallow("selectOperation"); }
    public void performOperation(ATMContext ctx) { ctx.disallow("performOperation"); }
    public void ejectCard(ATMContext ctx) { ctx.disallow("ejectCard"); }
}

class HasCardState implements State {
    public void insertCard(ATMContext ctx, int cardNumber) { ctx.disallow("insertCard"); }

    public void enterPin(ATMContext ctx, int pin) {
        if (!ctx.currentCard.validatePin(pin)) throw new IllegalStateException("Invalid PIN");
        ctx.setState(new AuthenticatedState());
        ctx.audit("PIN_VALIDATED", "ok=true");
    }

    public void selectOperation(ATMContext ctx, OperationType t, Map<String, Object> p) { ctx.disallow("selectOperation"); }
    public void performOperation(ATMContext ctx) { ctx.disallow("performOperation"); }

    public void ejectCard(ATMContext ctx) {
        ctx.audit("CARD_EJECT", "");
        ctx.clearSession();
        ctx.setState(new IdleState());
    }
}

class AuthenticatedState implements State {
    public void insertCard(ATMContext ctx, int cardNumber) { ctx.disallow("insertCard"); }
    public void enterPin(ATMContext ctx, int pin) { ctx.disallow("enterPin"); }

    public void selectOperation(ATMContext ctx, OperationType type, Map<String, Object> params) {
        ctx.currentOperation = ctx.operationFactory.getOperation(type, ctx, params);
        ctx.setState(new TransactionState());
        ctx.audit("OP_SELECTED", "type=" + type);
    }

    public void performOperation(ATMContext ctx) { ctx.disallow("performOperation"); }

    public void ejectCard(ATMContext ctx) {
        ctx.audit("CARD_EJECT", "");
        ctx.clearSession();
        ctx.setState(new IdleState());
    }
}

class TransactionState implements State {
    public void insertCard(ATMContext ctx, int cardNumber) { ctx.disallow("insertCard"); }
    public void enterPin(ATMContext ctx, int pin) { ctx.disallow("enterPin"); }
    public void selectOperation(ATMContext ctx, OperationType t, Map<String, Object> p) { ctx.disallow("selectOperation"); }

    public void performOperation(ATMContext ctx) {
        if (ctx.currentOperation == null) throw new IllegalStateException("No operation selected");
        ctx.currentOperation.perform();
        ctx.audit("OP_DONE", "");
        // remain authenticated and allow another selection if desired
        ctx.currentOperation = null;
        ctx.setState(new AuthenticatedState());
    }

    public void ejectCard(ATMContext ctx) {
        ctx.audit("CARD_EJECT", "");
        ctx.clearSession();
        ctx.setState(new IdleState());
    }
}

// ======================= Context =======================
class ATMContext {
    // shared components
    final AccountManager accountManager;
    final CardManager cardManager;
    final CashInventory cashInventory;
    final DispenserChain dispenserChain = new DispenserChain();
    final OperationFactory operationFactory = new OperationFactory();

    // session state
    State state = new IdleState();
    Card currentCard;
    Performable currentOperation;

    public ATMContext(AccountManager am, CardManager cm, CashInventory ci) {
        this.accountManager = am;
        this.cardManager = cm;
        this.cashInventory = ci;
    }

    // State transitions
    public void setState(State s) { this.state = s; }

    // API
    public void insertCard(int cardNumber) { state.insertCard(this, cardNumber); }
    public void enterPin(int pin) { state.enterPin(this, pin); }
    public void selectOperation(OperationType type, Map<String, Object> params) { state.selectOperation(this, type, params); }
    public void performOperation() { state.performOperation(this); }
    public void ejectCard() { state.ejectCard(this); }

    // helpers
    void disallow(String action) { throw new IllegalStateException("Action not allowed in state " + state.getClass().getSimpleName() + ": " + action); }
    void clearSession() { currentCard = null; currentOperation = null; }

    void audit(String event, String detail) {
        // Replace with real audit sink
        System.out.println("[AUDIT] " + event + " " + detail);
    }
}

// ======================= Demo Driver =======================
public class LLD_ATM {
    public static void main(String[] args) {
        // set up repos
        AccountManager am = new AccountManager();
        CardManager cm = new CardManager();
        Account acc = new Account("A-001", 980);
        am.add(acc);
        Card card = new Card(123456, "A-001", 4321);
        cm.add(card);

        // load cash
        CashInventory ci = new CashInventory();
        ci.load(100, 5); // 500
        ci.load(50,  4); // 200
        ci.load(10,  8); // 80
        System.out.println("ATM loaded: " + ci.totalAmount());

        ATMContext atm = new ATMContext(am, cm, ci);

        // flow: insert card -> pin -> balance -> withdraw 230 -> eject
        atm.insertCard(123456);
        atm.enterPin(4321);

        atm.selectOperation(OperationType.BALANCE_ENQUIRY, Map.of());
        atm.performOperation();

        atm.selectOperation(OperationType.WITHDRAW, Map.of("amount", 230));
        atm.performOperation();

        atm.selectOperation(OperationType.BALANCE_ENQUIRY, Map.of());
        atm.performOperation();

        atm.ejectCard();

        System.out.println("ATM remaining: " + ci.totalAmount());
    }
}
