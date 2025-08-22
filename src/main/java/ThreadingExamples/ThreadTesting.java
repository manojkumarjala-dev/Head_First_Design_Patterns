package ThreadingExamples;
import java.lang.Thread;
class TestThread extends Thread{
    @Override
    public void run() {
        for(int i = 0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+" for "+i+"th time from thread." );
        }
    }
}

class TestRunnableThread implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getState());
        for(int i = 0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+" for "+i+"th time from thread." );
        }
    }
}

class InterruptingThread extends Thread{
    @Override
    public void run(){
        try {
            for (int i=0;i<1000;i++){
                Thread.sleep(1000);
                System.out.println("Printing "+i);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted my sleep:" +e.getMessage());
        }
    }
}
public class ThreadTesting {
    public static void main(String[] args) throws InterruptedException{
        // printing out current thread;
        // System.out.println(Thread.currentThread().getName());
        //Thread exampleThread = new TestThread();
        //exampleThread.start();
        //Thread runnableThread = new Thread(new TestRunnableThread());
        //runnableThread.start();
        //Thread.sleep(100);
        InterruptingThread interruptingThread = new InterruptingThread();
        interruptingThread.start();
        Thread.sleep(1000);
        interruptingThread.interrupt();

    }
}

/*
Main functions available
- start()
- yield() -> when this present inside thread's execution it lets the JVM know that the thread is ready to
             give execution chance for other threads.
- join()  -> when called upon a thread t1, inside other thread t2, where t1 is started then t2 will wait
             till t1 finishes up.

- setPriority() : NORM(5), HIGH(10), LOW(1)

- interrupt() : when called from main thread or any thread it interrupts whatever the thread is executing
 */