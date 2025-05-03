package com.javabasics.JavaStreamFeatures.multithreading.problems;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ThreadPool implements Runnable {
    /*
    Thread Pool
Description: Implement a thread pool, which manages a set of worker threads that execute a set of tasks.
The pool should manage a queue of tasks, assign them to idle threads, and ensure that threads are recycled after task completion.
Key Concepts: Task queuing, thread management, worker threads.
Extension: Implement mechanisms for resizing the thread pool dynamically depending on the workload.
     */
    int numberOfTask = 0;

    ThreadPool(int numberOfTask){
        this.numberOfTask = numberOfTask;
    }

    @Override
    public void run(){
        System.out.println("Task is started in "+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task completed : "+numberOfTask);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Object lock = new Object();
        int num = sc.nextInt();
        Thread[] arr = new Thread[num];

        Queue<Runnable> q = new LinkedList<>();
        for(int i=0;i<10;i++){
            q.add(new ThreadPool(i+1));
        }

        for(int i=0;i<num;i++){
            helper(arr[i], q, lock);
        }
//        for(int i=0;i<num;i++){
//            helper(arr[i], q, lock);
//        }

    }

    public static void helper(Thread t, Queue<Runnable> q, Object lock){
        while(!q.isEmpty()){
            synchronized (lock){
//                t = q.remove();
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}



class ThreadPoolChatGPT {
    // Task Queue to store the tasks
    private final Queue<Runnable> taskQueue;

    // Worker Threads
    private final WorkerThread[] workerThreads;

    // Constructor to initialize thread pool
    public ThreadPoolChatGPT(int numThreads) {
        taskQueue = new LinkedList<>();
        workerThreads = new WorkerThread[numThreads];

        // Create and start worker threads
        for (int i = 0; i < numThreads; i++) {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start(); // Start the worker thread
        }
    }

    // Submit a task to the pool
    public synchronized void submitTask(Runnable task) {
        taskQueue.offer(task);
        notify(); // Notify one of the threads that a task is available
    }

    // Worker thread class
    private class WorkerThread extends Thread {
        public void run() {
            while (true) {
                Runnable task = null;
                synchronized (ThreadPoolChatGPT.this) {
                    while (taskQueue.isEmpty()) {
                        try {
                            // Wait for a task to be available
                            ThreadPoolChatGPT.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Get the task from the queue
                    task = taskQueue.poll();
                }

                // Execute the task
                if (task != null) {
                    task.run();
                }
            }
        }
    }

    // Main function to demonstrate the thread pool
    public static void main(String[] args) {
        // Create a thread pool with 3 worker threads
        ThreadPoolChatGPT threadPool = new ThreadPoolChatGPT(3);

        // Submit some tasks to the pool
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.submitTask(() -> {
                System.out.println("Task " + taskId + " is being processed by " + Thread.currentThread().getName());
                try {
                    // Simulate task work
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task " + taskId + " is completed by " + Thread.currentThread().getName());
            });
        }
    }
}
