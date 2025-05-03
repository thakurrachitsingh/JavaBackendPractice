package com.javabasics.JavaStreamFeatures.multithreading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;


//1. Java 7 approach
public class PrintEvenOddNumbersUsing2Threads implements Runnable {

    static int count = 1;
    Object object;

    PrintEvenOddNumbersUsing2Threads(Object object){
        this.object = object;
    }

    @Override
    public void run() {
        while(count<=10){
            if(count%2!=0 && Thread.currentThread().getName().equals("odd")){
                synchronized(object){
                    System.out.println("This is even thread : "+count);
                    count++;
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else if(count%2==0 && Thread.currentThread().getName().equals("even")){
                synchronized(object){
                    System.out.println("This is odd thread : "+count);
                    count++;
                    object.notify();
                }
            }
        }
    }

    public static void main(String[] args){
        Object lock = new Object();
        PrintEvenOddNumbersUsing2Threads r1 = new PrintEvenOddNumbersUsing2Threads(lock);
        PrintEvenOddNumbersUsing2Threads r2 = new PrintEvenOddNumbersUsing2Threads(lock);

        new Thread(r1, "even").start();
        new Thread(r2, "odd").start();
//        t1.start();
//        t2.start();
//        Thread t1 = new Thread(r1);
    }
}


//2. Java 8 approach with executor service
class PrintEvenOddNumbersUsingExecutorService{

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.rangeClosed(1, 10).forEach(num ->{

            CompletableFuture<Integer> cf1 = CompletableFuture.completedFuture(num).thenApplyAsync(x->{
                if(x%2==0){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("This is Even : "+num);
                }
                return num;
            }, executorService);
            cf1.join();
//            System.out.println(cf1.join());
            CompletableFuture<Integer> cf2 = CompletableFuture.completedFuture(num).thenApplyAsync(x->{
                if(x%2!=0){
                    System.out.println("This is odd : "+num);
                }
                return num;
            }, executorService);
            cf2.join();
        });
        executorService.shutdown();
    }
}


class UsingJava8PredicateInterface{

    static final Object lock = new Object();

    static IntPredicate pEven = x -> x%2 == 0;
    static IntPredicate pOdd = x -> x%2 != 0;

    public static void execute(IntPredicate condition){
        IntStream.rangeClosed(1, 10).filter(condition).forEach(UsingJava8PredicateInterface::printNumber);
    }

    public static void printNumber(int i) {
        synchronized (lock){
            try{
                System.out.println("This is"+Thread.currentThread().getName()+" "+i);
                lock.notify();
                lock.wait();
            }catch (Exception e){
                System.out.println("error : "+e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

            CompletableFuture<Void> cf1 = CompletableFuture.runAsync(()-> UsingJava8PredicateInterface.execute(pOdd));
//            cf1.join();
            CompletableFuture<Void> cf2 = CompletableFuture.runAsync(()-> UsingJava8PredicateInterface.execute(pEven));
//            cf2.join();
        Thread.sleep(1000);
    }
}
