package com.javabasics.JavaStreamFeatures.FreechargePrep2.Java8.Stream;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamPractice {

    public static void main(String[] args) {

        String input = "ilovehavatechie";
        int[] input2 = {5, 9, 11, 7, 22, 16, 3, 8, 10, 15, 6, 12, 20, 18, 4, 14, 19, 17, 13, 21};
        String[] input3 = {"apple", "banana", "orange", "grape", "kiwi", "melon", "berry", "peach", "plum", "pear", "mango", "papaya", "fig", "date", "avocado", "coconut", "pomegranate", "lychee", "guava", "passionfruit"};

//        //1. Count the occurences of each character in a string and print the result as a map.
        Map<String, Long> ans = Arrays.stream(input.split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(ans);

        //2. Find all the duplicate characters from the string and print them as a list.
//        List<String> result = Arrays.stream(input.split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).toList();

        //3.Find first non-repeating character in the string and print it.
//        String result = Arrays.stream(input.split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().filter(entry -> entry.getValue() == 1).findFirst().get().getKey();

        //4. Find the second-largest number in the given array and print it.
//        Integer result = Arrays.stream(input2).boxed().sorted(Comparator.reverseOrder()).skip(1).limit(1).findFirst().get();

        //5. Group the strings in the array by their length and print the result as a map.




        //6. Find the longest String in the array and print it. // converted to hashmap
//        String result = Arrays.stream(input3).reduce((a, b) -> a.length() < b.length() ? b : a).map(entry-> Map.entry(entry, entry.length())).get().toString();


        //7. Find the average length of the strings in the array and print it.
//        String result = Arrays.stream(input3).;



        //8. Find numbers that start with 1 in the given array and print them as a list.
        List<String> result = Arrays.stream(input2).boxed().map(a -> a + "").filter(a -> a.charAt(0) == '1').toList();

        //9.

        System.out.println(result);
    }
}
