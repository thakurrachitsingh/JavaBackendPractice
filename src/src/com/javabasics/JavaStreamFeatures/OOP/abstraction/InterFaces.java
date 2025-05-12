package com.javabasics.JavaStreamFeatures.OOP.abstraction;

public class InterFaces implements A, B {


    @Override
    public void same() {

    }

    @Override
    public void different2() {

    }

    @Override
    public void different1() {

    }
}

interface A{

    default void same(){

    }

    void different1();
}
interface B{

    void same();
    void different2();
}
