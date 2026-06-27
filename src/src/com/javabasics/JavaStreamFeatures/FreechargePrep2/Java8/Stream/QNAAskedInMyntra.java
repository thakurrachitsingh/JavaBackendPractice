package com.javabasics.JavaStreamFeatures.FreechargePrep2.Java8.Stream;

import java.util.*;
import java.util.stream.Collectors;

public class QNAAskedInMyntra {

    public static void main(String[] args) {

        Map<String, Integer> map = Map.of("a", 1000, "b", 2000, "c", 30000, "d", 40000, "e", 40000);

        //1. Find the nth highest salary from the map and print it.
        int n = 1;
//        Map.Entry<String, Integer> result = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).skip(n).findFirst().get();

        //2. Includes duplicate salaries in question 1.
        Map.Entry<Integer, List<String>> result = map.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList()))).entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).skip(n).findFirst().get();


        //3. 
        System.out.println(result);

    }


}
