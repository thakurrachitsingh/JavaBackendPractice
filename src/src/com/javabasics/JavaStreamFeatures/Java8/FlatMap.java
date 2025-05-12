package com.javabasics.JavaStreamFeatures.Java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMap {

    public static void main(String[] args) {
        List<Employee> lst=  new ArrayList<>(Arrays.asList(
                new Employee(1, "Rachit", List.of("7310849972", "8409636004")),
                new Employee(2, "Naman", List.of("1234567891", "9987654321"))
        ));

        //1. FlatMap
//        List<String> stringStream = lst.stream().flatMap(employee -> employee.getPhoneNumbers().stream()).collect(Collectors.toList());
//        System.out.println(stringStream);

        //2. Find maximum occurence of any character using stream api.
//        String s = "anonymous";
//        Map<String, Long> collect = Arrays.stream(s.split("")).collect(Collectors.groupingBy(
//                Function.identity(), Collectors.counting()
//        ));
//        System.out.println(collect);

        //3.


    }



}

class Employee{
    long id;
    String name;
    List<String> phoneNumbers;

    public Employee(long id, String name, List<String> phoneNumbers) {
        this.id = id;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
