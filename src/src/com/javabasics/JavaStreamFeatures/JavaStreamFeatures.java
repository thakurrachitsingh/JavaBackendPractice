package com.javabasics.JavaStreamFeatures;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaStreamFeatures {

    public static void main(String[] args){


        //1. Stream::ofNullable
        //2. Stream.iterate()
        //3. Collectors.collectingAndThen()
        //4. takeWhile() and dropWhile()
        //5. Collectors.teeing() - Used to run multiple comparator in parallel/same time
        //6. stream().concat()
        //7. Collectors.partitionBy()
        //8. IntStream for ranges




        //4. takeWhile() and dropWhile()

        List<Integer> lst = List.of(1,2,4,5,6,7,8,9,3);

        List<Integer> resLst= lst.stream().takeWhile(t -> t<4).collect(Collectors.toList());
        System.out.println("4. "+resLst);

        //5. Collectors.teeing() - Used to run multiple comparator in parallel/same time

        List<Integer> teeingLst = List.of(1,2,4,5,6,7,8,9,3);
        Object teeingRes = teeingLst.stream().collect(Collectors.teeing(
                Collectors.maxBy(Integer::compareTo),
                Collectors.minBy(Integer::compareTo),
                (num1, num2) -> Map.of("Max", num1.get(), "Min", num2.get())
        ));
        System.out.println("5. "+teeingRes);

        //6. stream().concat()

        Stream<Integer> stream1 = Stream.of(1, 2, 3);
        Stream<Integer> stream2 = Stream.of(4, 5, 6);
        List<Integer> streamConcatRes = Stream.concat(stream1, stream2).collect(Collectors.toList());
        System.out.println("6. "+streamConcatRes);
    }
}
