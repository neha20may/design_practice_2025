package com.example.design_problems.service.java_collection_revise;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class collections_demo {
    static class ConsumerEg implements Consumer<Integer> {
        @Override
        public void accept(Integer a) {
            System.out.println(a);
        }
    }

    static class ConsumerEg2 implements IntConsumer {
        @Override
        public void accept(int i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {

        //collection
        List<Integer> l = new ArrayList<>();
        Stack<Integer> st = new Stack<>();
        PriorityQueue<Integer> p = new PriorityQueue<>(); //heap
        Set<Integer> hs = new HashSet<>(); //its literally hashmap inside and yet its inside collections ; individual elements collection
        TreeSet<Integer> ts = new TreeSet<>();//its built on treemap- bst/red back tree
        Deque<Integer> dq = new ArrayDeque<>();
        //map
        Map<Integer, String> m = new HashMap<>();
        Map<Integer, String> lm = new LinkedHashMap<>();
        Map<Integer, String> tm = new TreeMap<>();
//perform these operations and more to understand the basic things
        //add
        //get
        //remove
        //print
        //modify
        //clone

//        arrayTesting();
//        System.out.println("---print before modification ");
//        print(l);
//        testArrayList(l);//arrays are copy values; since these are already references these are essentially copy by ref!
//        System.out.println("---print after modification ");
//        print(l);

//        testStack(st);
//        print(st);
//        testHeap(p);
//        testSet(hs);
//        testTreeSet(ts);
//        testDeque(dq);

//        testMap(m);
        //linked hashmap - dequeue + hash - heavliy used in lru
//       testTreeMap(lm);


    }

    private static void testTreeMap(Map<Integer, String> lm) {
        lm.put(1, "a");
        lm.put(2, "b");
        Set<Map.Entry<Integer, String>> set= lm.entrySet();
        System.out.println("--");
        set.stream().forEach(System.out::println);//ordered
        lm.remove(1);
        System.out.println("---");
        set.forEach(System.out::println);
        lm.put(3, "c");
        System.out.println("---");
        set.forEach(System.out::println);
        lm.putIfAbsent(2, "testUpdate");
        System.out.println("---");
        set.forEach(System.out::println);
        lm.put(2, "forceModify");
        System.out.println("--");
        set.forEach(System.out::println);
        String v= lm.get(2);
        String v2= lm.getOrDefault(4, "default");
        System.out.println(v+" "+v2);
    }

    private static void testMap(Map<Integer, String> m) {
        m.put(1, "a");
        m.put(2, "b");
        Map<Integer, String> m2 = new HashMap<>(m);
        System.out.println(m);
        System.out.println(m2);
        if(m.containsKey(1)){
            System.out.println("cool");
        }else{
            System.out.println("fool");
        }

        Set<Map.Entry<Integer, String>> entryObj = m.entrySet();
        entryObj.stream().forEach((Map.Entry<Integer, String> entry) -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            entry.setValue("random assignment " + entry.getKey());
            System.out.println(entry);
        });
        System.out.println(m);
        System.out.println(m2);
        m.remove(1);
        m.remove(2);
        if (m.isEmpty()) {
            System.out.println("empty");
        }
        System.out.println(m2.get(1));
        System.out.println(m2.getOrDefault(3, "default"));
        BiConsumer<Integer, String> biObj= new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                System.out.println("key ="+integer+" val ="+s);
            }
        };
        m.forEach(biObj);
        m.forEach((k,v)-> System.out.println("k="+k+"v="+v));
    }

    private static void testDeque(Deque<Integer> dq) {
        //typical queue - offer peek remove add ; at both end operations allowed
        dq.add(1);
        dq.add(2);
        dq.addFirst(0);
        dq.addLast(3);
        System.out.println(dq);
        System.out.println(dq.poll());
        System.out.println(dq);
        System.out.println(dq.pollFirst());
        System.out.println(dq);
        System.out.println(dq.pollLast());
        System.out.println(dq);
        Integer head = dq.remove();
        System.out.println(head);
        dq.add(100);
        dq.add(101);
        Integer tail = dq.removeLast();
        Integer head2 = dq.removeFirst();
        System.out.println(head2 + " " + tail);
        dq.add(102);
        dq.push(103);
        dq.push(104);
        dq.push(105);
        System.out.println(dq.pop());
    }

    private static void testTreeSet(TreeSet<Integer> ts) {
        ts.add(1);
        ts.add(2);
        ts.add(5);
        ts.add(0);
        ts.add(5);
        System.out.println(ts);//ordered
        ts.remove(1);
        System.out.println(ts);
        TreeSet<Integer> ts2 = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                return y - x;
            }
        });
        ts2.addAll(ts);
        System.out.println(ts2);
    }

    private static void testSet(Set<Integer> hs) {
        System.out.println("hashset ------");
        hs.add(300000);
        hs.add(200);
        hs.add(200);
        hs.add(100000);
        System.out.println(hs);
        hs.remove(1);
        hs.remove(new Integer(2));
        System.out.println(hs);
        HashSet<Integer> hs2 = new HashSet<>(hs);
        System.out.println(hs2);
        List<Integer> list = Stream.of(1, 2, 3, 4, 4, 5).collect(Collectors.toList());
        hs2 = new HashSet<>(list);
        System.out.println(hs2);
        //reverse to list
        Stream<Integer> stream = hs2.stream();
        List<Integer> listData = stream.collect(Collectors.toList());
        System.out.println(listData);
        //reverse to Integer []
        Stream<Integer> streamInt = hs2.stream();
        Integer[] revList = streamInt.toArray(Integer[]::new);//stream closes here ; to Array is terminal
        //reverse list to prim int
        Stream<Integer> streamInt2 = Arrays.stream(revList); //or hs2.stream()
        IntStream intStream = streamInt2.mapToInt(x -> {
            return x;
        }); //unbox
        int[] data = intStream.toArray();
        System.out.println(data); //address
        for (int i : data) {
            System.out.print(i);
        }
    }

    private static void testHeap(PriorityQueue<Integer> p) {
        p.add(1);
        p.add(2);
        p.add(3);
        p.offer(4);
        System.out.println(p.peek());
        System.out.println(p.poll());
        System.out.println(p.poll());
        p.remove(1); //object removal- nothing special implements the collections remove(Object o)
        p.remove(3);
        Integer head = p.remove();//removes the first element as it retuend by poll
        System.out.println(p.peek());
        p.add(5);
        p.add(6);
        p.add(7);
        PriorityQueue<Integer> p2 = new PriorityQueue<>(Comparator.reverseOrder());


        p2.addAll(p);
        System.out.println("--- p2 ----");
        while (!p2.isEmpty()) {
            System.out.println(p2.poll());
        }
        System.out.println("--- p1---");
        while (!p.isEmpty()) {
            System.out.println(p.poll());
        }
        Comparator<Integer> obj = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        };
        PriorityQueue<Integer> p3 = new PriorityQueue<>((x, y) -> {
            return y - x;
        });
        p3.add(1);
        p3.add(2);
        p3.add(3);
        System.out.println("--p3 ---");
//        while(!p3.isEmpty()){
//            System.out.println(p3.poll());
//        }

        p3.remove(2);
        while (!p3.isEmpty()) {
            System.out.println(p3.poll());
        }
//        List<Integer> l= new ArrayList<>({1,2,3}); doesnt work
        List<Integer> l = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
        p3 = new PriorityQueue<>(l);
        System.out.println(" p3 from list ");
        while (!p3.isEmpty()) {
            System.out.println(p3.poll());
        }

    }

    private static void testStack(Stack<Integer> st) {
        st.add(1);
        st.add(2);
        st.add(3);
        System.out.println(st.peek());
//        int top= st.pop();
//        System.out.println(top);
//        Integer top2= st.pop();
//        System.out.println(top2);
        print(st);//1, 2, 3
        st.add(4); //vector method - its parent; same effect as poush
        System.out.println(st.peek());

        st.remove(1);//like list -- vector behaviour since this is built on list
        st.remove(new Integer(1));//--list
        st.push(5);
        System.out.println(st.peek());

        while (!st.isEmpty()) {
            System.out.println(st.pop());
        }
        List<Integer> list = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
//        Stack<Integer> st2= new Stack<>(list); --- this doesnt work !!phew

        Stack<Integer> st2 = new Stack<>();
        st2.addAll(list); //Collection method!!

    }

    private static void testArrayList(List<Integer> l) {
        l.add(1);
        l.add(2);
        List<Integer> l2 = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
        print(l);
        print(l2);

        l.remove(1);
        l2.remove(1); //specic to list since these are index based structures
        System.out.println("-----remove test index--");
        print(l);
        print(l2);

        l.remove(new Integer(1));
        l2.remove(new Integer(3));
        System.out.println("---remove valye ----");
        print(l); //didnt print anything
        print(l2);

        l.add(0, 1);
        l2.add(0, 10);
        System.out.println("----add index ---");
        print(l);
        print(l2);

        l.set(0, -10);
        l2.set(1, 100);
        System.out.println("----set index---");
        print(l);
        print(l2);

        List<Integer> copyList = new ArrayList<>(l);
        System.out.println("-- copy ---");
        print(copyList);

        IntStream is = l.stream().mapToInt(x -> {
            return x;
        });
        int[] intarr = is.toArray();
        System.out.println("-- to int[] -- unboxing --");
        print(intarr);

        //TODO: sort

    }

    private static void print(List<Integer> l) {
        Stream<Integer> s = l.stream(); //in arrays it was Arrays.stream(arr);
        ConsumerEg eg = new ConsumerEg();
        s.forEach(eg);
    }

    private static void arrayTesting() {
        Integer[] arr = new Integer[10];
        Integer[] arr2 = {1, 2, 3, 4, 4};
        //        arr.add(2);
        arr[1] = 2;
//        arr[11]=2;
        System.out.println("this is only ene elemet init of array");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("this is normal stream print of array");
        Stream<Integer> s = Arrays.stream(arr);
        ConsumerEg cobj = new ConsumerEg();
        s.forEach(cobj);

        int[] arr3 = {1, 2, 3, 4, 5};
        IntStream intStream = Arrays.stream(arr3);
        ConsumerEg2 ceg2 = new ConsumerEg2();
        intStream.forEach(ceg2);

        //remove
        //shift the elemnts by myself?
        add(arr, 3, 3);
        update(arr, 5, 5);
        print(arr);
        remove(arr, 3);
        /**
         * int [] --> Integer [] == both are array
         * Step 1: Arrays.stream ==IntStream
         * Step 2: .boxed() = Stream<Integer> //operation defined in intstream
         * Step 3: .toArray() = Integer [] //.toArray operationdefined in stream
         *
         *
         * note:
         * Arrays.stream(int[]) returns IntStream
         * Arrays.stream(Integer []) returns Stream<Integer>
         */
        IntStream intStream2 = Arrays.stream(arr3);
        Stream<Integer> convertedStream = intStream2.boxed();
        Integer[] carr = convertedStream.toArray(Integer[]::new);
//        Object[] carr2 = convertedStream.toArray();

        Stream<Integer> c1 = Arrays.stream(arr3).boxed();
//        String[] sarr= c1.toArray(String[]::new);
//        print(sarr);
        add(carr, 3, 3);
        print(carr);

        IntStream ointS = Arrays.stream(carr).mapToInt(x -> {
            return x;
        });
        int[] backtoO = ointS.toArray();
        print(backtoO);
        //TODO: sort
    }

    private static void print(int[] backtoO) {
        for (int i : backtoO) {
            System.out.println(i);
        }
    }

    static void add(Integer[] a, int i, int v) {
        a[i] = v;
    }

    static void update(Integer[] a, int i, int v) {
        a[i] = v;
    }

    static void remove(Integer[] a, int i) {
        if (i > a.length - 1 || i < 0) {
            System.out.println("cant remove; index>N || i<0");
        }
        for (int j = i + 1; j < a.length; j++) {
            a[j - 1] = a[j];
        }
        a[a.length - 1] = null;
    }

    static void print(Integer[] a) {
        Stream<Integer> s = Arrays.stream(a);
        s.forEach((x) -> {
            System.out.println(x);
        });
    }

    static void print(String[] a) {
        Stream<String> s = Arrays.stream(a);
        s.forEach((x) -> {
            System.out.println(x);
        });
    }
}
