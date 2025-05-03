package com.javabasics.JavaStreamFeatures.multithreading.problems;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerProblem {
    /*
    1. Producer-Consumer Problem
Description: There are two types of threads: producers and consumers. Producers produce items and place them in a shared buffer,
while consumers take items from the buffer and consume them. Ensure that access to the buffer is synchronized properly so that
consumers don't consume an item before it’s produced, and producers don’t try to add items to a full buffer.
Key Concepts: Blocking queues, semaphores, locks, thread synchronization.
Extensions: Use semaphores or monitors to control access to the shared buffer. You may also be asked to implement different
buffer sizes (e.g., bounded vs unbounded).
    * */

    static IntBuffer intBuff;
    static List<Integer> lst = new ArrayList<>();
    static int capacity;
    static Object lock = new Object();
    ProducerConsumerProblem(int capacity){
        ProducerConsumerProblem.capacity = capacity;
    }

    public static void produce(){
        int value = 1;
        int repeat = 10;
        while(repeat-->0){
            synchronized (lock){
                if(lst.size()>=capacity){
                    System.out.println("Buffer is full");
                    lock.notify();
                    try {
                        lock.wait(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    lst.add(value);
                    System.out.println("Produced Data : "+value);
                    value++;
                    lock.notify();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void consume(){
        int repeat = 20;
        while(repeat-->0){
            synchronized (lock){
                if(lst.size()==0){
                    System.out.println("Buffer is empty");
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    int consumedData = lst.remove(0);
                    System.out.println("Consumed Data : "+consumedData);
                    lock.notify();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        ByteBuffer buff = ByteBuffer.wrap(arr);
        ExecutorService ex = Executors.newFixedThreadPool(2);
//        CompletableFuture cfProducer = CompletableFuture.runAsync(()-> new ProducerConsumerProblem(5).consume(), ex);
//        CompletableFuture cfConsumer = CompletableFuture.runAsync(()-> new ProducerConsumerProblem(5).produce(), ex);
        ex.submit(()-> new ProducerConsumerProblem(5).consume());
        ex.submit(()-> new ProducerConsumerProblem(5).produce());
//        ex.awaitTermination(2000, TimeUnit.MILLISECOND);
        ex.shutdown();
//        Thread.sleep(10000);
//        cfProducer.join();
//        cfConsumer.join();
//        cfConsumer.complete(new Object());
//        cfProducer.complete(new Object());
    }


}



//Solution on gfg
class Geeks {
    public static void main(String[] args)
            throws InterruptedException
    {
        // Object of a class that has both produce()
        // and consume() methods
        final PC pc = new PC();

        // Create producer thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // t1 finishes before t2
        t1.join();
        t2.join();
    }

    // This class has a list, producer (adds items to list)
    // and consumer (removes items).
    public static class PC {
        // Create a list shared by producer and consumer
        // Size of list is 2.
        LinkedList<Integer> list = new LinkedList<>();
        int capacity = 2;

        // Function called by producer thread
        public void produce() throws InterruptedException
        {
            int value = 0;
            while (true) {
                synchronized (this)
                {
                    // producer thread waits while list is full
                    if (list.size() == capacity) {
                        System.out.println("List is full, producer is waiting...");
                        // Signal any waiting consumer before waiting
                        notify();
                        wait();
                    }

                    // to insert the jobs in the list
                    list.add(value);
                    System.out.println("Producer produced-" + value);
                    value++;

                    // notifies the consumer thread that now it can start consuming
                    notify();

                    // makes the working of program easier to understand
                    Thread.sleep(1000);
                }
            }
        }

        // Function called by consumer thread
        public void consume() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    // consumer thread waits while list is empty
                    if (list.size() == 0) {
                        System.out.println("List is empty, consumer is waiting...");
                        // Signal any waiting producer before waiting
                        notify();
                        wait();
                    }

                    // to retrieve the first job in the list
                    int val = list.removeFirst();
                    System.out.println("Consumer consumed-" + val);

                    // Wake up producer thread
                    notify();

                    // and sleep
                    Thread.sleep(1000);
                }
            }
        }
    }
}
