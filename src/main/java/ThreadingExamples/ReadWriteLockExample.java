package ThreadingExamples;

import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

class BankAccount {
    int balance;

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock readLock = readWriteLock.readLock();
    Lock writeLock = readWriteLock.writeLock();
    //Lock normalLock = new ReentrantLock();

    BankAccount(int balance) {
        this.balance = balance;
    }

    void updateBalance(){
        System.out.println(Thread.currentThread().getName() + " trying to acquire write lock...");
        writeLock.lock();
        try{
            System.out.println(Thread.currentThread().getName() + " successfully acquired write lock. Updating balance...");
            for(int i =0;i<10;i++){
                balance-=1;
            }
            System.out.println("Updated Balance: "+balance);
        }
        finally {
            System.out.println(Thread.currentThread().getName() + " released write lock.");
            writeLock.unlock();
        }
    }

    void getBalance() {
        System.out.println(Thread.currentThread().getName() + " trying to acquire read lock...");

        readLock.lock(); // This will not block other threads from acquiring the read lock.
        //normalLock.lock(); // Run this, you will not see the print st-1 for multiple threads immediately
        try {
  /*st-1*/  System.out.println(Thread.currentThread().getName() + " successfully acquired read lock. Reading balance...");
            Thread.sleep(50); // Simulating time taken to read the balance
            for(int i =0;i<10;i++){
                System.out.println("Balance: "+balance+ " from "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " releasing read lock.");
            //normalLock.unlock();
            System.out.println(Thread.currentThread().getName() + " released read lock.");
        }
    }
}

public class ReadWriteLockExample {
    public static void main(String[] args) throws InterruptedException {
        BankAccount bankAccount = new BankAccount(100);

        Runnable readTask = bankAccount::getBalance;
        Runnable writeTask = bankAccount::updateBalance;


        Thread t1 = new Thread(readTask, "Thread-1");
        Thread t2 = new Thread(readTask, "Thread-2");
        Thread t3 = new Thread(writeTask, "Thread-3");

        t3.start();
        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }
}
