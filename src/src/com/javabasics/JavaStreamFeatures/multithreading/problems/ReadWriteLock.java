package com.javabasics.JavaStreamFeatures.multithreading.problems;

public class ReadWriteLock {
    /*
    Read-Write Lock
Description: Implement a read-write lock, where multiple readers can access the resource simultaneously,
but writers need exclusive access. The problem is to avoid issues where multiple writers could try to write simultaneously.
Key Concepts: Reader-Writer locks, thread synchronization, fairness in locking.
Extension: Optimize for performance and avoid deadlock in highly concurrent systems.
     */

    private int count = 0;
    private Object readLock;
    private Object writeLock;

    public int read(){
        synchronized (writeLock){
            System.out.println(Thread.currentThread().getName()+" performing read");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count;
        }
    }

    public void write() throws InterruptedException {
        synchronized (writeLock){
            System.out.println(Thread.currentThread().getName()+" performing write");
            Thread.sleep(1000);
            count++;
        }
    }

    ReadWriteLock(Object readLock, Object writeLock){
        this.readLock = readLock;
        this.writeLock = writeLock;
    }

    public static void main(String[] args) throws InterruptedException {
        Object left = new Object();
        Object right = new Object();
        Thread t1 = new Thread(()->{
            try {
                new ReadWriteLock(left, right).write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(()->{
            new ReadWriteLock(left, right).read();
        });
        t2.start();
        Thread t3 = new Thread(()->{
            new ReadWriteLock(left, right).read();
        });
        t3.start();
        Thread t4 = new Thread(()->{
            try {
                new ReadWriteLock(left, right).write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t4.start();
        Thread t5 = new Thread(()->{
            new ReadWriteLock(left, right).read();
        });
        t5.start();
        Thread t6 = new Thread(()->{
            new ReadWriteLock(left, right).read();
        });
        t6.start();
    }

}
