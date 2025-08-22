package VendingMachine;

import java.util.*;

public class VendingMachine {

    // --- Demo ---
    public static void main(String[] args) {
        VendingMachineContext vm = new VendingMachineContext(10);
        vm.updateInventory(new Item("SODA", 65), 1, 3);
        vm.updateInventory(new Item("CHIPS", 50), 2, 1);
        vm.updateInventory(new Item("WATER", 40), 3, 2);

        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.DOLLAR);
        vm.startProductSelection();
        vm.selectProduct(1); // buy SODA, expect change 60

        vm.insertCoin(Coin.QUARTER);
        vm.startProductSelection();
        vm.selectProduct(2); // insufficient funds

        vm.insertCoin(Coin.DOLLAR);
        vm.startProductSelection();
        vm.selectProduct(2); // now ok

        vm.insertCoin(Coin.DIME);
        vm.refund(); // refund remaining
    }

    // --- Context ---
    static class VendingMachineContext {
        private VendingMachineState state;
        private final Inventory inventory;
        private final List<Coin> coins = new ArrayList<>();
        private Integer selectedCode = null;

        VendingMachineContext(int slotCapacity) {
            this.inventory = new Inventory(slotCapacity);
            setState(new IdleState());
        }

        // public API: delegate to state
        public void insertCoin(Coin c)             { state.insertCoin(this, c); }
        public void startProductSelection()        { state.startSelection(this); }
        public void selectProduct(int code)        { state.selectProduct(this, code); }
        public void dispense()                     { state.dispense(this); }
        public void updateInventory(Item it,int code,int count){ state.updateInventory(this, it, code, count); }
        public void refund()                       { state.refund(this); }

        // helpers for states
        void addCoin(Coin c) {
            coins.add(c);
            System.out.println("Inserted: " + c + " (" + c.value + ")");
            System.out.println("Balance: " + getBalance());
        }
        int getBalance() {
            int s = 0; for (Coin c : coins) s += c.value; return s;
        }
        void clearCoins() { coins.clear(); }
        Inventory getInventory() { return inventory; }
        void setSelectedCode(Integer code) { selectedCode = code; }
        Integer getSelectedCode() { return selectedCode; }
        void clearSelection() { selectedCode = null; }
        void setState(VendingMachineState s) {
            this.state = s;
            System.out.println("State -> " + s.getClass().getSimpleName());
        }
    }

    // --- State interface ---
    interface VendingMachineState {
        default void insertCoin(VendingMachineContext ctx, Coin c) {
            System.out.println("Cannot insert coin in " + getClass().getSimpleName());
        }
        default void startSelection(VendingMachineContext ctx) {
            System.out.println("Cannot start selection in " + getClass().getSimpleName());
        }
        default void selectProduct(VendingMachineContext ctx, int code) {
            System.out.println("Cannot select product in " + getClass().getSimpleName());
        }
        default void dispense(VendingMachineContext ctx) {
            System.out.println("Cannot dispense in " + getClass().getSimpleName());
        }
        default void updateInventory(VendingMachineContext ctx, Item it, int code, int count) {
            System.out.println("Cannot update inventory in " + getClass().getSimpleName());
        }
        default void refund(VendingMachineContext ctx) {
            System.out.println("Cannot refund in " + getClass().getSimpleName());
        }
    }

    // --- Concrete States ---
    static final class IdleState implements VendingMachineState {
        @Override public void insertCoin(VendingMachineContext ctx, Coin c) {
            ctx.addCoin(c);
            ctx.setState(new HasMoneyState());
        }
        @Override public void updateInventory(VendingMachineContext ctx, Item it, int code, int count) {
            ctx.getInventory().addItem(it, code, count);
            System.out.println("Stocked " + it.type() + " x" + count + " at slot " + code);
        }
    }

    static final class HasMoneyState implements VendingMachineState {
        @Override public void insertCoin(VendingMachineContext ctx, Coin c) {
            ctx.addCoin(c); // stay in HasMoney
        }
        @Override public void startSelection(VendingMachineContext ctx) {
            ctx.setState(new SelectionState());
        }
        @Override public void refund(VendingMachineContext ctx) {
            int amount = ctx.getBalance();
            if (amount > 0) System.out.println("Refunding: " + amount);
            ctx.clearCoins();
            ctx.clearSelection();
            ctx.setState(new IdleState());
        }
    }

    static final class SelectionState implements VendingMachineState {
        @Override public void selectProduct(VendingMachineContext ctx, int code) {
            try {
                Item item = ctx.getInventory().peekItem(code);
                int price = item.price();
                int bal = ctx.getBalance();
                if (bal < price) {
                    System.out.println("Insufficient funds. Price " + price + ", paid " + bal);
                    return;
                }
                ctx.setSelectedCode(code);
                ctx.setState(new DispenseState());
                ctx.dispense(); // proceed to dispense
            } catch (NoSuchElementException e) {
                System.out.println("Invalid slot " + code);
            } catch (IllegalStateException e) {
                System.out.println("Sold out at slot " + code);
            }
        }
    }

    static final class DispenseState implements VendingMachineState {
        @Override public void dispense(VendingMachineContext ctx) {
            Integer code = ctx.getSelectedCode();
            if (code == null) {
                System.out.println("No product selected");
                ctx.setState(new IdleState());
                return;
            }
            try {
                Item item = ctx.getInventory().dispenseOne(code);
                System.out.println("Dispensing: " + item.type());
                int change = ctx.getBalance() - item.price();
                if (change > 0) System.out.println("Returning change: " + change);
            } catch (Exception e) {
                System.out.println("Dispense failed at slot " + code + ": " + e.getMessage());
            } finally {
                ctx.clearCoins();
                ctx.clearSelection();
                ctx.setState(new IdleState());
            }
        }
    }

    // --- Support types ---
    enum Coin {
        PENNY(1), NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100);
        public final int value;
        Coin(int v) { this.value = v; }
    }

    record Item(String type, int price) { }

    static final class Inventory {
        private final int capacity;
        private final Map<Integer, Slot> slots = new HashMap<>();
        Inventory(int capacity) { this.capacity = capacity; }

        void addItem(Item item, int code, int count) {
            validateCode(code);
            if (count <= 0) throw new IllegalArgumentException("count must be > 0");
            Slot s = slots.get(code);
            if (s == null) { s = new Slot(item, 0); slots.put(code, s); }
            else if (!s.item.equals(item)) { s.item = item; }
            s.count += count;
        }

        Item peekItem(int code) {
            validateCode(code);
            Slot s = slots.get(code);
            if (s == null) throw new NoSuchElementException("empty slot");
            if (s.count <= 0) throw new IllegalStateException("sold out");
            return s.item;
        }

        Item dispenseOne(int code) {
            validateCode(code);
            Slot s = slots.get(code);
            if (s == null || s.count <= 0) throw new IllegalStateException("sold out");
            s.count -= 1;
            return s.item;
        }

        private void validateCode(int code) {
            if (code < 1 || code > capacity) throw new NoSuchElementException("invalid code");
        }

        static final class Slot {
            Item item;
            int count;
            Slot(Item item, int count) { this.item = item; this.count = count; }
        }
    }
}
