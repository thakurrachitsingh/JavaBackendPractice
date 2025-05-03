package com.javabasics.JavaStreamFeatures.multithreading.problems;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Race Condition: Counter
Description: You have multiple threads incrementing a shared counter variable. Write a program that demonstrates a race
condition (where threads’ updates interfere with each other) and then fix the race condition using locks or atomic variables.
 */
public class RaceConditionCounter extends Thread {

    private Counter counter;

    RaceConditionCounter(Counter c){
        this.counter = c;
    }

    @Override
    public void run() {
        int times = 1000;
        while(times-->0){
            counter.increment();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter c = new Counter();
        Thread t1 = new RaceConditionCounter(c);
        Thread t2 = new RaceConditionCounter(c);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(c.getCount());
    }
}

class Counter{
    int count = 0;
    public synchronized void increment(){
        count++;
    }
    public int getCount(){
        return count;
    }
}
