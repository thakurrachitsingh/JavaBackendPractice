package com.javabasics.JavaStreamFeatures.multithreading.problems;

public class ReadWriteImplementation implements JReadWriteI {

    int readers;
    int writers;
    int writeRequests;

    @Override
    public void lockRead() throws InterruptedException {
        while(writers>0 || writeRequests>0){
            wait();
        }
        readers++;
    }

    @Override
    public void unlockRead() {
        readers--;
        notifyAll();
    }

    @Override
    public void lockWrite() throws InterruptedException {
        writeRequests++;
        while(readers>0 || writers>0){
            wait();
        }
        writers++;
        writeRequests--;
    }

    @Override
    public void unlockWrite() {
        writers--;
        notifyAll();
    }
}

interface JReadWriteI{

    void lockRead() throws InterruptedException;

    void unlockRead();

    void lockWrite() throws InterruptedException;

    void unlockWrite();
}
