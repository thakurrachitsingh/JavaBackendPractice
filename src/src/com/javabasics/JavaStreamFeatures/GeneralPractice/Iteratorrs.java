package com.javabasics.JavaStreamFeatures.GeneralPractice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Iteratorrs {

    public static void main(String[] args){
        ArrayList<String> lst = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Iterator<String> it = lst.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        for (Iterator<String> iter = it; iter.hasNext(); ) {
            String s = iter.next();
            System.out.println(s);
        }

    }
}
