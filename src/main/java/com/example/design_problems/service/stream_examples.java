package com.example.design_problems.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.of;

public class stream_examples {


    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        List<Integer> l2 = Stream.of(1, 2, 3).collect(Collectors.toList());

        Stream<Integer> s = l.stream();
        s.forEach(x -> System.out.println(x));

        Stream<Integer> s2 = l.stream().filter(x -> x > 2);
        s2.forEach(System.out::print);
        System.out.println();
        Stream<String> s3 = l.stream().filter(x -> x > 2).map(x -> x + ":: ");
        s3.forEach(System.out::print);
        System.out.println();
        Boolean res = l.stream().anyMatch(x -> x > 2);
        System.out.println(res);

        Integer r = l.stream().findAny().get();
        System.out.println(r);

        r = l.stream().reduce((acc, y) -> {
            return acc + y;
        }).get();
        System.out.println(r);

        r = l.stream().reduce(1000, (a, b) -> {
            return a + b;
        }); //no get here
        System.out.println(r);

        System.out.println(l.stream().collect(Collectors.toList()));
        System.out.println(l.stream().collect(Collectors.toSet()));

        Function<Integer, String> keymapperObj = new Function<Integer, String>() {
            @Override
            public String apply(Integer t) {
                return "a";
            }
        };

        Function<Integer, String> valuemapperObj= new Function<Integer, String> (){
            @Override
            public String apply(Integer i){
                return "b";
            }
        };
        l.stream().collect(Collectors.toMap(keymapperObj, valuemapperObj));

    }

}
