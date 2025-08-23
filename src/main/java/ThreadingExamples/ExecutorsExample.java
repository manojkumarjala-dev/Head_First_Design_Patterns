package ThreadingExamples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;



public class ExecutorsExample {
    public static void main(String[] args){
        //Executors holds some predefined functions to create a thread pool for us.
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // this is also valid because the submit function is overloaded
        // Functional Interface Callable is being referred here which can return Future of any type
        Future<Integer> result = executorService.submit(()->42);


        for(int i =0;i<10;i++){
            int finalI = i+1;
            executorService.submit(()->{
                long ans = factorial(finalI);
                System.out.println("Factorial of "+finalI+": "+ans);
            });
        }
        executorService.shutdown();
        try {
            /*
            The awaitTermination function's purpose is to block the calling thread until
            all tasks in an ExecutorService have completed execution after a shutdown request,
            or until a "specified timeout"(10 seconds in our case) occurs,
            or the current thread is interrupted.
             */
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Finished");

    }

    static int factorial(int n){
        int result = 1;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i =1;i<=n;i++) result*=i;

        return result;
    }
}
