package com.javabasics.JavaStreamFeatures.multithreading.problems;


/*
Implementing a Countdown Latch
Description: Implement a countdown latch where multiple threads wait for a particular event or condition to be met before
proceeding. This is often used for waiting for threads to complete tasks before proceeding.
 */

public class CountDownLatch {

}

class MyCountDownLatch{

    int count;

    MyCountDownLatch(int count){
        this.count = count;
    }

    public synchronized void await() throws InterruptedException {
        while(count>0){
            wait();
        }
    }

    public synchronized void countDown(){
        if(count>0){
            count--;
            if(count==0){
                notifyAll();
            }
        }
    }

}


class LatchTest {
    public static void main(String[] args) {
        final int NUM_WORKERS = 3;
        MyCountDownLatch latch = new MyCountDownLatch(NUM_WORKERS);

        Runnable worker = ( ) -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is doing work...");
            try {
                Thread.sleep((int)(Math.random() * 1000)); // simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(threadName + " finished work.");
            latch.countDown(); // signal completion
        };

        // Start worker threads
        for (int i = 0; i < NUM_WORKERS; i++) {
            new Thread(worker, "Worker-" + (i+1)).start();
        }

        // Main thread waits
        System.out.println("Main thread is waiting for workers to finish...");
        try {
            latch.await(); // block until count == 0
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("All workers finished. Main thread proceeds.");
    }
}
