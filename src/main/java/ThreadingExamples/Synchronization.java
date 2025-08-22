package ThreadingExamples;
class Counter{
    int counter;
    Counter(int counter){
        this.counter = counter;
    }

    void incrementCounter(){
        this.counter++;
    }

    int getCounter(){
        return this.counter;
    }
}

class SynchronizedCounter {
    int counter;
    SynchronizedCounter(int counter){
        this.counter = counter;
    }
    // synchronized keyword here is restricting access of both threads into the function.
    // if a thread goes in, it only will execute other thread when this thread exits this function.
    // most important synchronized is used on the resource not inside thread
    synchronized void incrementCounter(){
        this.counter++;
    }

    int getCounter(){
        return this.counter;
    }
}

class ThreadExample extends Thread{

    Counter counter;
    ThreadExample(Counter counter){
        this.counter = counter;
    }

    @Override
    public void run(){
        for(int i =0;i<10000;i++){
            counter.incrementCounter();
        }
    }
}

class SynchronizedThreadExample extends Thread{

    SynchronizedCounter counter;
    SynchronizedThreadExample(SynchronizedCounter counter){
        this.counter = counter;
    }

    @Override
    public void run(){
        for(int i =0;i<10000;i++){
            counter.incrementCounter();
        }
    }
}

public class Synchronization {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter(0);
        Thread t1 = new ThreadExample(counter);
        Thread t2 = new ThreadExample(counter);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Interleaving the thread execution is cause reading of stale counter value
        // This critical section need to be guarded
        // two ways: 1. synchronized function, 2. synchronized block
        System.out.println("Expected counter Value: 20000");
        System.out.println("Resulted counter Value for ThreadExample: "+counter.getCounter());

        SynchronizedCounter synchronizedCounter = new SynchronizedCounter(0);

        Thread t3 = new SynchronizedThreadExample(synchronizedCounter);
        Thread t4 = new SynchronizedThreadExample(synchronizedCounter);

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Resulted counter Value for SynchronizedThreadExample: "+synchronizedCounter.getCounter());

    }
}
