package ThreadingExamples;

class Resource{
    int data;
    boolean hasData;

    synchronized void produce(){
        while(hasData){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        data++;
        System.out.println("Produced: "+data);
        notify();
        hasData=true;
    }

    synchronized void consume(){
        while(!hasData){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        hasData=false;
        System.out.println("Consumed: "+data);
        notify();
    }
}

class Producer implements Runnable{
    Resource resource;
    Producer(Resource resource){
        this.resource = resource;
    }

    @Override
    public void run() {

        for(int i =0;i<10;i++) resource.produce();

    }
}

class Consumer implements Runnable{
    Resource resource;
    Consumer(Resource resource){
        this.resource = resource;
    }

    @Override
    public void run() {

        for(int i =0;i<10;i++) resource.consume();

    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {

        Resource resource = new Resource();
        Runnable producer = new Producer(resource);
        Runnable consumer = new Consumer(resource);

        Thread t1 = new Thread(producer,"Thread-1");
        Thread t2 = new Thread(consumer,"Thread-2");


        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

}
