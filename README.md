# Java — Design Patterns & Low Level Design

Java implementations of classic design patterns and real-world Low Level Design (LLD) problems commonly asked in software engineering interviews at top companies.

Built while working through *Head First Design Patterns* (Freeman & Robson) and practicing LLD interview problems from scratch.

---

## Why this repo exists

LLD interviews test whether you can translate a real-world system into clean, extensible object-oriented code — using the right patterns, the right abstractions, and the right structure. This repo is my practice ground for that.

---

## Design Patterns

From *Head First Design Patterns*, implemented chapter by chapter in Java.

| Chapter | Pattern | Problem it solves |
|---|---|---|
| Chapter 1 & 2 (v1) | **Strategy** | Defines a family of algorithms, encapsulates each one, and makes them interchangeable at runtime |
| Chapter 1 & 2 (v2) | **Observer** | Notifies multiple dependent objects automatically when one object changes state |
| Chapter 1 & 2 (v2 extended) | **Observer (extended)** | Extends observer with push/pull models and multiple subject types |
| Chapter 3 | **Decorator** | Attaches new behaviors to objects at runtime without modifying their class |
| Chapter 5 | **Singleton** | Ensures a class has only one instance with a global access point |
| Chapter 6 (v1) | **Command** | Encapsulates a request as an object, enabling undo, queuing, and logging |
| Chapter 6 (v2/3) | **Command (advanced)** | Extends command pattern with macro commands and undo stacks |
| Chapter 7 (v1) | **Adapter** | Wraps an incompatible interface so it works with code expecting a different one |
| Chapter 7 (v2) | **Facade** | Provides a simplified interface to a complex subsystem |
| Chapter 8 | **Template Method** | Defines the skeleton of an algorithm, letting subclasses fill in the steps |

---

## Low Level Design — Interview Problems

Real-world systems designed from scratch using object-oriented principles and design patterns.

| System | Concepts covered |
|---|---|
| **Chess Game** | Board abstraction, piece hierarchy, move validation, game state management |
| **Snake and Food** | Grid-based game loop, collision detection, object modeling |
| **Snake and Ladder** | Turn-based game engine, dice abstraction, board design |
| **Connect Four** | Grid state, win condition detection, player abstraction |
| **Tic Tac Toe** | Board representation, game loop, extensible win logic |
| **Parking Lot** | Multi-level parking, vehicle types, spot allocation, ticketing |
| **Parking Lot Manager** | Extended parking lot with manager workflows and edge cases |
| **Elevator System** | Request scheduling, direction logic, multi-elevator coordination |
| **ATM Machine** | State machine design, transaction flow, card and cash handling |
| **Vending Machine** | State machine design, inventory management, payment processing |

---

## Concurrency & Multithreading

| Module | Concepts covered |
|---|---|
| **ConcurrencyControl** | Synchronization, locks, thread-safe patterns |
| **MyHashMap** | Custom HashMap implementation with thread safety |
| **ThreadingExamples** | Thread pool, ExecutorService, common concurrency patterns |
| **VendingMachine** | Concurrency-safe state machine implementation |

---

## Project Structure

```
src/
└── main/
    └── java/
        ├── Chapter1/              # Strategy pattern
        ├── Chapter2/              # Observer pattern
        ├── Chapter2_v2/           # Observer pattern (extended)
        ├── Chapter3/              # Decorator pattern
        ├── Chapter5/              # Singleton pattern
        ├── Chapter6_v1/           # Command pattern
        ├── Chapter6_v2_3/         # Command pattern (advanced)
        ├── Chapter7_v1/           # Adapter pattern
        ├── Chapter7_v2_facade/    # Facade pattern
        ├── Chapter8_template/     # Template method pattern
        ├── ChessGame/             # LLD - Chess
        ├── SnakeAndFood/          # LLD - Snake and Food
        ├── SnakeAndLadder/        # LLD - Snake and Ladder
        ├── ConnectFour/           # LLD - Connect Four
        ├── TicTacToeGame/         # LLD - Tic Tac Toe
        ├── ParkingLot/            # LLD - Parking Lot
        ├── ParkingLotManager/     # LLD - Parking Lot (extended)
        ├── ElevatorSystem/        # LLD - Elevator System
        ├── LLD_ATM/               # LLD - ATM Machine
        ├── VendingMachine/        # LLD - Vending Machine
        ├── ConcurrencyControl/    # Multithreading
        ├── MyHashMap/             # Custom HashMap + thread safety
        └── ThreadingExamples/     # Thread pool & ExecutorService
```

---

## How to Run

```bash
# Clone the repo
git clone https://github.com/manojkumarjala-dev/Head_First_Design_Patterns.git

# Build with Maven
mvn compile

# Run any specific class (example)
mvn exec:java -Dexec.mainClass="ChessGame.Main"
```

---

## Reference

- *Head First Design Patterns* — Freeman & Robson (O'Reilly)
- *Design Patterns: Elements of Reusable Object-Oriented Software* — Gang of Four

---

## About Me

Full stack engineer with a backend core. I work primarily in Java, Spring Boot, and distributed systems.

[LinkedIn](https://linkedin.com/in/manojkumarjala) · [LeetCode](https://leetcode.com/u/manojkumarjala) · [Portfolio](https://manojkumarjala-portfolio.vercel.app)
