# ATM Low-Level Design (LLD) — Interview-Style Walkthrough

> **Goal:** Show how a candidate thinks, clarifies requirements, narrows scope, and evolves a clean object model with appropriate design patterns. This mirrors a 45–60 minute whiteboard LLD interview.

---

## 0) Elevator Pitch

Design an **ATM** that supports: **insert card → validate PIN → select operation → execute (withdraw / balance enquiry / change PIN) → eject card**. The core is a **finite state machine** with clean separation between UI, business logic, and persistence, plus a **Chain of Responsibility** to dispense cash (100/50/10).

---

## 1) Requirements Clarification (What I Ask the Interviewer)

**Functional**

* Insert card, read card, validate PIN.
* Supported ops: withdraw cash, balance enquiry, change PIN.
* Cash inventory management per ATM; denominations: 100, 50, 10.
* Withdraw constraints: multiples of 10, sufficient account balance, sufficient ATM cash, optional daily limit.
* Session: perform multiple operations while authenticated; eject card at any time.

**Non‑Functional**

* Reliability: no partial debits; atomicity for dispense+debit.
* Extensibility: add denominations/operations with minimal change.
* Testability: logic not tied to console I/O; pure domain methods.
* Security basics: PIN hashing (demo), audit trail, session timeout.

**Out of Scope (explicitly stated)**

* Networking with bank switch, real HSM/crypto, multi-ATM replication in detail, receipt printing, cash deposit with note validation.

---

## 2) First Cut Model — Naive Approach (and Why It’s Weak)

* One big `ATM` class with methods: `insertCard`, `enterPin`, `withdraw`, etc.
* Problems found in review:

    * Methods valid only in certain phases → implicit state bugs.
    * Hard to test: interleaved I/O and logic.
    * Extending with new ops/denoms touches many places (OCP violation).

**Insight:** Make states explicit; push shared, mutable data into a **Context**.

---

## 3) Evolving the Design — Key Decisions

1. **State Pattern** for session flow

    * States: `Idle`, `HasCard`, `Authenticated`, `Transaction`.
    * Each state exposes the same API but enforces allowed actions.

2. **Context Object** (`ATMContext`)

    * Holds shared session+infrastructure: current card, selected operation, `AccountManager`, `CardManager`, `CashInventory`, `DispenserChain`.
    * States are stateless w\.r.t. data; they only mutate context and transition.

3. **Operations as Strategy** (via Factory)

    * `Performable` interface with `WithdrawMoney`, `BalanceEnquiry`, `ChangePin`.
    * `OperationFactory` builds an operation from type + params → **open for extension**.

4. **Cash Dispense via Chain of Responsibility (CoR)**

    * `100 → 50 → 10` nodes try to fulfill amount; pass remainder along chain.
    * Keeps denomination logic modular; easy to add `20` later.

5. **Repositories / Managers**

    * `CardManager` resolves card and checks PIN.
    * `AccountManager` loads/updates account balance. (In-memory for demo; could wrap DB.)

6. **I/O Separation**

    * No `Scanner`/console in domain methods; a UI/driver layer gathers inputs and calls pure APIs.

---

## 4) Core Entities & Relationships

* **Account**: `accountId`, `balance`.
* **Card**: `cardNumber`, `accountId`, `pinHash`.
* **CashInventory**: `Map<Value, Count>` with `load`, `totalAmount`, `deduct`.
* **DispenserChain**: head of `Dispenser` nodes (100, 50, 10) providing `canDispense` and `dispenseAndDeduct`.
* **ATMContext**: orchestrator; holds `state`, `currentCard`, `currentOperation`, managers, inventory.
* **State**: interface with methods: `insertCard`, `enterPin`, `selectOperation`, `performOperation`, `ejectCard`.
* **Performable**: `perform()`; concrete ops implement business rules.

**High-Level Relation (textual UML)**

```
ATMContext "1" o-- "1" State
ATMContext "1" o-- "1" CashInventory
ATMContext "1" o-- "1" DispenserChain
ATMContext "1" o-- "1" AccountManager
ATMContext "1" o-- "1" CardManager
State <|.. IdleState
State <|.. HasCardState
State <|.. AuthenticatedState
State <|.. TransactionState
Performable <|.. WithdrawMoney
Performable <|.. BalanceEnquiry
Performable <|.. ChangePin
Dispenser <|.. HundredDispenser
Dispenser <|.. FiftyDispenser
Dispenser <|.. TenDispenser
```

---

## 5) State Machine (Allowed Actions)

| State             | insertCard | enterPin | selectOperation | performOperation | ejectCard |
| ----------------- | ---------- | -------- | --------------- | ---------------- | --------- |
| **Idle**          | ✅          | ❌        | ❌               | ❌                | ❌         |
| **HasCard**       | ❌          | ✅        | ❌               | ❌                | ✅         |
| **Authenticated** | ❌          | ❌        | ✅               | ❌                | ✅         |
| **Transaction**   | ❌          | ❌        | ❌               | ✅                | ✅         |

**Transitions**

* `Idle → HasCard` on insert card
* `HasCard → Authenticated` on correct PIN
* `Authenticated → Transaction` after selecting an operation
* `Transaction → Authenticated` after operation completes (allow chaining)
* Any state → `Idle` on eject card (clears session)

---

## 6) Withdraw Flow (Happy Path)

1. `insertCard(cardNo)` → context loads `Card`.
2. `enterPin(pin)` → validate → state → `Authenticated`.
3. `selectOperation(WITHDRAW, {amount})` → builds `WithdrawMoney`.
4. `performOperation()`

    * Fetch `Account` by card.accountId.
    * Validate amount multiple of 10.
    * Check `balance >= amount`.
    * Check `dispenserChain.canDispense(amount, cashInventory)`.
    * `dispenserChain.dispenseAndDeduct(...)` → atomically consume notes.
    * `account.debit(amount)`.
    * `audit` and return bundle map.
5. Optionally do more ops; `ejectCard()` clears session.

**Error Paths**

* Invalid PIN → remain in `HasCard`; allow retry policy.
* Insufficient funds or cash → throw domain error; stay authenticated.
* Invalid amount → validation error.

---

## 7) Design Patterns (Why They Fit)

* **State**: Prevents illegal calls in wrong phase; readable transitions; isolates session rules.
* **Strategy/Factory (Operations)**: Pluggable behaviors for different operations; new ops don’t touch existing ones.
* **Chain of Responsibility (Dispense)**: Composable denomination handling; easy addition of `20` without touching others.
* **Repository (Managers)**: Swappable persistence; aids testing.

---

## 8) Extensibility Hooks

* **New denomination**: Add `TwentyDispenser`, link into chain; add `cashInventory.load(20, x)`.
* **New operation**: Create `TransferFunds implements Performable`; add to factory.
* **Daily limits**: Add `WithdrawalPolicy` checked inside `WithdrawMoney`.
* **Audit/Telemetry**: Replace `audit()` with async sink (Kafka) and idempotent write.
* **Timeouts**: Session timer in `ATMContext`; auto-eject on idle.

---

## 9) Concurrency & Atomicity (Interview Talking Points)

* Single ATM → single-threaded session, but still protect **cash inventory** updates and **account debits** with a small critical section.
* If account store is remote: perform **debit** and **dispense decision** as a small **transaction**. If DB: single transaction; if external core bank: use a **two-step**: authorize debit → lock funds → dispense → capture.
* Idempotency keys for retries (network blips).

---

## 10) Testing Strategy

* **Unit**: State transition tests; Withdraw happy/insufficient/cannot-dispense; CoR planning.
* **Property tests**: Sum(notes\*value) equals dispensed amount; cash never negative.
* **Contract tests**: `AccountManager` and `CardManager` fakes.

---

## 11) Reference Code (Single File Demo)

A complete Java demo with context/state, operations, and dispenser chain is available in the companion snippet (shared in chat). It compiles and runs an end-to-end flow: **insert → PIN → balance → withdraw → balance → eject**.

---

## 12) What I’d Say in the Room (Narration)

* “I’ll start with **requirements** and mark items out-of-scope to ensure we don’t boil the ocean.”
* “The user journey is a **finite sequence**; I’ll model it as a **State Machine**. I’ll push shared data into a **Context** to avoid leaking session into states.”
* “Operations are **pluggable** behaviors; I’ll use a **Factory** to construct them with parameters, keeping the context clean.”
* “Dispensing cash is **orthogonal**; I’ll implement a **CoR** so denominations are modular. That gives me easy future-proofing.”
* “I’m avoiding console I/O in domain code to keep it **testable**. The driver/UI will pass parameters into state methods.”
* “Time permitting, I’d add daily limits, receipt printing, and session timeouts; the model has clear extension points.”

---

## 13) Quick ASCII Sequence (Withdraw)

```
User → ATMContext.insertCard
ATMContext → CardManager.getByNumber → Card
State: Idle → HasCard
User → ATMContext.enterPin
HasCard → validatePin → Authenticated
User → selectOperation(WITHDRAW,{amount}) → OperationFactory → WithdrawMoney
Authenticated → Transaction
User → performOperation()
WithdrawMoney → AccountManager.getById
WithdrawMoney → DispenserChain.canDispense + dispenseAndDeduct
WithdrawMoney → Account.debit
Transaction → Authenticated (allow next op)
User → ejectCard → Idle
```

---

## 14) Trade‑offs & Alternatives (Good Discussion Points)

* **Greedy CoR vs DP for dispense**: Greedy works for canonical 100/50/10; introduce DP if adding non-canonical denominations (e.g., 70).
* **State explosion**: Could fold `Transaction` into `Authenticated` with a sub‑state, but the explicit state makes rule enforcement simpler.
* **Persistence**: In-memory managers for demo; production would use repositories with DB transactions and external bank connectors.

---

## 15) Closing

This design keeps **illegal flows impossible**, isolates **cash logic**, and stays **open for extension**. It’s easy to demo, unit test, and evolve under interview time constraints.
