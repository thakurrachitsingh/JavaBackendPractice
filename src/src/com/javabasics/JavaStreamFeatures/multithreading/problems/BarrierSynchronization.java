package com.javabasics.JavaStreamFeatures.multithreading.problems;


import java.util.concurrent.CyclicBarrier;

/*
Barrier Synchronization
Description: You are given N threads that need to work in parallel. However, all threads must reach a certain point (a barrier)
before any thread can proceed. The goal is to implement this barrier synchronization.
 */
public class BarrierSynchronization {


}

class Task extends Thread{

    private int time;
    private CyclicBarrier barrier;
    Task(int time, CyclicBarrier barrier){
        this.time = time;
        this.barrier = barrier;
    }
    @Override
    public void run(){
        try{
            Thread.sleep(time);
            System.out.println("waiting at barrier");
            barrier.await();
            System.out.println("wait complete");
        }catch(Exception e){
            System.out.println("Caught Exception : "+e);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier barrier = new CyclicBarrier(2, ()->{
            System.out.println("All Threads are reached");
        });
        Thread t1 = new Task(3000, barrier);
        Thread t2 = new Task(2000, barrier);
        t1.start();
        t2.start();
        t2.join();
    }
}
