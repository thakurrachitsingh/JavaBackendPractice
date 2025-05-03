package com.javabasics.JavaStreamFeatures.multithreading.problems;

import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersProblem implements Runnable {

    /*
    2. Dining Philosophers Problem
    Description: There are N philosophers sitting around a table. Each philosopher thinks and eats. To eat, a philosopher needs two
    forks, one on either side. The challenge is to implement a solution where no philosopher will starve and deadlock is avoided.
Key Concepts: Deadlock, starvation, resource allocation, synchronization.
Extension: Modify the problem for N threads where the last philosopher doesn't share a fork with the first one (in case of odd
number philosophers) to avoid deadlocks.
     */

    private final ReentrantLock left;
    private final ReentrantLock right;
    private final int philosopherNumber;

    DiningPhilosophersProblem(ReentrantLock left, ReentrantLock right, int philosopherNumber){
        this.left = left;
        this.right = right;
        this.philosopherNumber = philosopherNumber;
    }

    @Override
    public void run() {
        System.out.println("started thread"+philosopherNumber);
        while(true){
            try{
                think();
                if(philosopherNumber%2==0){
                    left.lock();
                    right.lock();
                }else{
                    right.lock();
                    left.lock();
                }
                eat();
                left.unlock();
                right.unlock();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public void think() throws InterruptedException {
        Thread.sleep(100);
    }

    public void eat() throws InterruptedException {
        System.out.println("Philosopher "+philosopherNumber+" is eating");
        Thread.sleep(100);
    }

    public static void main(String[] args){
        Scanner sc=  new Scanner(System.in);
        int numberOfPhilosoper = sc.nextInt();
        ReentrantLock[] lockArray = new ReentrantLock[numberOfPhilosoper];

        for(int i=0;i<numberOfPhilosoper;i++){
            lockArray[i] = new ReentrantLock();
        }

        for(int i=0;i<numberOfPhilosoper;i++){
            ReentrantLock left = lockArray[i];
            ReentrantLock right = lockArray[(i+1)%numberOfPhilosoper];
            Thread t = new Thread(new DiningPhilosophersProblem(left, right, i+1));
            t.start();
        }

    }

}



//Solution by chat gpt
class DiningPhilosophers {

    static class Philosopher implements Runnable {
        private final int id;
        private final ReentrantLock leftChopstick;
        private final ReentrantLock rightChopstick;

        public Philosopher(int id, ReentrantLock leftChopstick, ReentrantLock rightChopstick) {
            this.id = id;
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    think();

                    if (id % 2 == 0) { // Even philosophers pick up left first
                        leftChopstick.lock();
                        rightChopstick.lock();
                    } else { // Odd philosophers pick up right first
                        rightChopstick.lock();
                        leftChopstick.lock();
                    }

                    eat();

                    leftChopstick.unlock();
                    rightChopstick.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking.");
            Thread.sleep((long) (Math.random() * 100));
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep((long) (Math.random() * 100));
        }
    }

    public static void main(String[] args) {
        int numPhilosophers = 5;
        ReentrantLock[] chopsticks = new ReentrantLock[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            chopsticks[i] = new ReentrantLock();
        }

        for (int i = 0; i < numPhilosophers; i++) {
            ReentrantLock left = chopsticks[i];
            ReentrantLock right = chopsticks[(i + 1) % numPhilosophers];
            Thread t = new Thread(new Philosopher(i, left, right));
            t.start();
        }
    }
}