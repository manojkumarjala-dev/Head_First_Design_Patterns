package ThreadingExamples;
/*
synchronized function did not block access for thread 2 which is executing the notifyExample() function
if u try
 */

import java.util.concurrent.Executor;

import static java.lang.Thread.sleep;

class SharedResource {
    synchronized void waitExample() {
        System.out.println(Thread.currentThread().getName() + " is waiting...");
        try {
            sleep(10000); // wont release lock and sleeps for given duration
            //wait() Releases the lock and waits
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " resumed after notify.");
    }


    synchronized void notifyExample() {
        System.out.println("Notifying a waiting thread...");
        //notifyAll(); // Wakes up one waiting thread if the function is wait()

    }
}


public class ThreadsExample1 {
    public static void main(String[] args) {
        SharedResource shared = new SharedResource();

        Executor executor = command -> new Thread(command).start();

        // Thread 1 (Waits)
        Thread t1 = new Thread(() -> shared.waitExample(), "Thread-1");

        // Thread 2 (Notifies after 2 seconds)


        //Thread t2 = new Thread(shared::waitExample, "Thread-2");
         /*

         The above will not execute because thread 1 blocked access
         => sleep() blocks access for other threads wanted to run the synchronized block
         => if wait() is present instead of sleep() then until we notify() and wake up the waiting cell it
         wont execute.

            Mechanism	    Holds Lock?	    Can Be Interrupted?	        Resumes On
            wait()	        ❌ No       	    ✅ Yes	            notify() / notifyAll()
            sleep()	        ✅ Yes	            ✅ Yes	            After timeout or interrupt()
            synchronized	✅ Yes	            N/A	                When method/block ends
          */


        Thread t2 = new Thread(shared::notifyExample, "Thread-2");
        /*

          This will always execute independednt of other synchronized function

          takeaway, synchronized block others and allow only one thread to execute it, if wait() is called
          releases lock, if sleep() will not release lock just sleeps for given duration
         */

//        Thread t3 = new Thread(() -> {
//            try {
//                sleep(2000); // Ensure Thread-1 goes to wait state
//                shared.notifyExample();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "Thread-3");



        t1.start();
        t2.start();
        //t3.start();
    }
}
