package com.javabasics.JavaStreamFeatures.multithreading.problems;

/*
Thread-safe Linked List
Description: Implement a thread-safe singly or doubly linked list. The list should support common operations like insert,
delete, and find, and should be safe to access concurrently by multiple threads.
Key Concepts: Locking mechanisms, concurrent data structures, atomicity.
Extension: Implement lock-free or lock-partitioned data structures for better performance.
 */

import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeLinkedList {
    public static void main(String[] args) {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        Runnable inserter = () -> {
            for (int i = 1; i <= 5; i++) {
                list.insert(i);
                System.out.println(Thread.currentThread().getName() + " inserted " + i);
            }
        };

        Runnable deleter = () -> {
            for (int i = 1; i <= 5; i++) {
                if (list.delete(i)) {
                    System.out.println(Thread.currentThread().getName() + " deleted " + i);
                }
            }
        };

        Thread t1 = new Thread(inserter, "Inserter-1");
        Thread t2 = new Thread(inserter, "Inserter-2");
//        Thread t3 = new Thread(deleter, "Deleter-1");

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

//        t3.start();
//        try {
//            t3.join();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        list.printList();
    }
}

//Threadsafe
class MyLinkedList<T>{
    MyNode head;
    ReentrantLock lock = new ReentrantLock();

    public void printList() {
        MyNode temp = head;
        while(temp!=null){
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    private static class MyNode<T>{
        T data;
        MyNode<T> next = null;
        MyNode(T data){
            this.data = data;
        }
    }

    public void insert(T data){
        lock.lock();
        try{
            if(head==null){
                head = new MyNode(data);
            }else{
                MyNode temp = head;
                while(temp.next!=null){
                    temp = temp.next;
                }
                temp.next = new MyNode(data);
            }
        }finally{
            lock.unlock();
        }
    }
    public boolean delete(T data){
        lock.lock();
        try{
            if(head==null) return false;
            else if(head.data==data){
                head = head.next;
                return true;
            }else{
                MyNode temp = head;
                while(temp.next!=null){
                    if(temp.next.data==data){
                        temp.next = temp.next.next;
                        return true;
                    }
                    temp = temp.next;
                }
            }
            return false;
        }finally{
            lock.unlock();
        }
    }
}
