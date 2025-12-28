package com.example.design_problems.service.java_collection_revise;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class myComp implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
        return o2 - o1;
    }
}

public class ArraysTest {

    public static void main(String[] args) {
        Integer[] myArr = new Integer[3];
        myArr[0] = 4;
        myArr[1] = 2;
        myArr[2] = 3;
        Stream<Integer> stream = Arrays.stream(myArr);
        stream.forEach(x -> System.out.println(x)); //rest indices are null

        int[] myArr2 = new int[10];
        myArr2[0] = 1;
        myArr2[1] = 1;
        IntStream stream2 = Arrays.stream(myArr2);
        stream2.forEach(x -> System.out.println(x)); //rest indices are 0 -- platform depdendent?

        List<Integer> myList = new ArrayList(10);
        myList.add(1);
        myList.add(2);
        myList.stream().forEach(x -> System.out.println(x));


        //Array operations
//        Arrays.sort(myArr);
//        Arrays.stream(myArr).forEach(x -> System.out.println(x));

        Comparator<Integer> compObj = new myComp();
        Comparator<Integer> newComp = new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) { //even public matters here- it should be exact syntax
                return a - b;
            }
        };
        Comparator<Integer> thirdComp = (a, b) -> {
            return b - a;
        };
        Arrays.sort(myArr, thirdComp);
        Arrays.stream(myArr).forEach(x -> System.out.println(x));

//        Comparator<int> intComp= new c //there is no compaarator over primitive
        Arrays.sort(myArr2);//this works; comparator does not
        Integer [] convArr2= Arrays.stream(myArr2).boxed().toArray(Integer[]::new);
        Arrays.sort(convArr2, thirdComp);
        Arrays.stream(convArr2).forEach(x-> System.out.println(x));

        //reverse them now
        //sort with opposite comparator
        Arrays.toString(myArr);

        //another way tp go in list realm
        List<Integer> listMyArr= Arrays.stream(myArr).collect(Collectors.toList());
        Collections.sort(listMyArr); //use collections
        Collections.sort(listMyArr, compObj);
        Consumer<? super Integer> print= x-> System.out.println(x);
        listMyArr.stream().forEach(print);
        Integer []origArr= listMyArr.stream().toArray(Integer[]::new);
        System.out.println(Arrays.toString(origArr));

        List<Integer> list= new ArrayList();
        //int []to integer[]
        //stream
        int [] arr= new int[5];
        IntStream iStream= Arrays.stream(arr);
        list = iStream.boxed().collect(Collectors.toList());
        Comparator<Integer> myComp= (a, b)->{
            return b-a;
        };
        Queue<Integer> pq= new PriorityQueue<Integer>(list);
        pq.addAll(list);


    }

}
