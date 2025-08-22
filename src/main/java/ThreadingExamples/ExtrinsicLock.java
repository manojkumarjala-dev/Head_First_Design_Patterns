package ThreadingExamples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class IntrinsicLockAccount{
    int balance;
    IntrinsicLockAccount(int balance){
        this.balance = balance;
    }

    synchronized void withdraw(int amount){
        if(balance>=amount){
            System.out.println("Attempting to withdraw - "+Thread.currentThread().getName());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            balance-=amount;
            System.out.println("Withdrawal successful, current balance:"+balance);
        }
        else{
            System.out.println("Insufficient Balance unable to process");
        }
    }
}

class ExtrinsicLockAccount{
    int balance;
    ExtrinsicLockAccount(int balance){
        this.balance = balance;
    }

    Lock lock = new ReentrantLock();

    void withdraw(int amount){
        try {

            if(lock.tryLock(1000, TimeUnit.MILLISECONDS)){
                if(balance>=amount){
                    System.out.println("Attempting to withdraw - "+Thread.currentThread().getName());

                    try {
                        Thread.sleep(5000);
                        balance-=amount;
                        System.out.println("Withdrawal successful, current balance:"+balance+" for "+Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    finally {
                        lock.unlock();
                    }
                }
                else{
                    System.out.println("Insufficient Balance unable to process");
                }
            }
            else{
                System.out.println("Unable to process request. Please try again later "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
public class ExtrinsicLock {
    static void intrinsicLockExample() throws InterruptedException{

        IntrinsicLockAccount intrinsicLockAccount = new IntrinsicLockAccount(100);
        Runnable task = () -> intrinsicLockAccount.withdraw(50);
        Thread t1 = new Thread(task,"Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
    static void extrinsicLockExample() throws InterruptedException{

        ExtrinsicLockAccount extrinsicLockAccount = new ExtrinsicLockAccount(100);
        Runnable task = () -> extrinsicLockAccount.withdraw(50);
        Thread t1 = new Thread(task,"Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
    public static void main(String[] args) throws InterruptedException {

        // No way to make thread 2 exit with Intrinsic lock ie: Synchronization
        // if the thread1 takes much time, we need to make thread-2 not wait unnecessarily
        // For this customization purpose Extrinsic Locks were introduced
        intrinsicLockExample();

        // if I customize the lock I can set like timeout time for thread.
        // tryLock(): lock not possible then will not wait
        // tryLock(int time, TimeUnit timeUnit): lock not acquired, will wait for time
        // lock() works same as synchronized wait till gets the lock
        extrinsicLockExample();

    }
}
