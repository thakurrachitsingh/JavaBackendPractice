package com.javabasics.JavaStreamFeatures.Java8;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo {
    public static void main(String[] args) {

        //1. Count occurance of Characters in string
        String s = "aaabncddbbbbx";
        var data = Arrays.stream(s.split("")).collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
        ));
        Optional<Map.Entry<String, Long>> max = data.entrySet().stream().max((a, b) -> (int) (a.getValue() - b.getValue()));
//        System.out.println(data);
//        System.out.println(max.get().getKey()+"="+max.get().getValue());

        Executor ex = Executors.newFixedThreadPool(2);
        CompletableFuture cp1 = CompletableFuture.supplyAsync(()->{
            return "result";
        }, ex);
        CompletableFuture cp2 = CompletableFuture.supplyAsync(()->{
            return "";
        }, ex);

        //1.2 give elements which are not distinct

//        var nonDistinct = data.entrySet().stream().distinct().collect(Collectors.toList());
//        var nonDistinct = data.entrySet().stream().filter(x-> x.getValue()>1).map(Map.Entry::getKey).collect(Collectors.toList());
//        System.out.println(nonDistinct);

        //1.3 Find first non-repeating character
//        var firstNonRepeating = Arrays.stream(s.split("")).collect(Collectors.groupingBy(
//                Function.identity(), LinkedHashMap::new, Collectors.counting()
//        )).entrySet().stream()
//                .filter(entry->
//                        entry.getValue()==1
//                ).findFirst();
//        System.out.println(firstNonRepeating);


        //2 Find second highest number from given array
//        int[] arr = {1, 2, 3, 4, 5, 6, 6, 4, 7, 8};
//        var sortedListOfDistinct = Arrays.stream(arr).boxed().distinct().sorted(Collections.reverseOrder()).collect(Collectors.toList());
//        System.out.println(sortedListOfDistinct.get(1));

        //3 Method to give largest string from the array
//        String[] str = {"Java", "Techie", "Spring", "Boot", "Microservices"};
//        var longestString = Arrays.stream(str).reduce((word1, word2)-> {
//            if(word1.length()>word2.length()){
//                return word1;
//            }else{
//                return word2;
//            }
//        });
//        System.out.println(longestString);

        //3 Find numbers in array that starts with 1
//        int[] arr = {1, 12, 14, 21, 31, 4, 7};
//        var listOfNumbersStartingWithOne =  Arrays.stream(arr).boxed()
//                .map(x-> x+"")
//                .filter(x-> x.charAt(0)=='1')
//                .collect(Collectors.toList());
//        System.out.println(listOfNumbersStartingWithOne);

        //4 Reduce the string to some format eg ("1-2-3-4-5")
        //a.
        int[] arr = {1, 2, 3, 4, 5, 6};
        var formattedIntoString = Arrays.stream(arr).boxed()
                .map(x-> x+"")
                .reduce((a, b)-> a+"-"+b).get();
        System.out.println(formattedIntoString);
        //b.
        List<String> lst = Arrays.asList("1", "2", "3", "4");
        System.out.println(String.join( "-", lst));

    }
}
