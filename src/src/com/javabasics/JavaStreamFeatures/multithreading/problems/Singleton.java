package com.javabasics.JavaStreamFeatures.multithreading.problems;

public class Singleton {

    public static void main(String[] args){
        //1. create enum
        HelperClass obj1 = HelperClass.INSTANCE;
        obj1.increment();
        HelperClass obj2 = HelperClass.INSTANCE;
        System.out.println(obj2.getNum());

        //2. Double-checked synchronized
        Helper2 t1 = Helper2.getInstance();
        Helper2 t2 = Helper2.getInstance();
        System.out.println(t1.hashCode()+" "+t2.hashCode());
    }

}


enum HelperClass{
    INSTANCE;

    int num = 0;

    public int getNum(){
        return num;
    }
    public void increment(){
        num++;
    }
}

class Helper2{
    private int num = 0;
    private static Helper2 instance;

    private Helper2(){}
    public static Helper2 getInstance(){
        synchronized (Helper2.class){
            if(instance==null){
                instance = new Helper2();
            }
        }
        return instance;
    }
}
