package com.example.design_problems.service;

import java.util.ArrayList;
import java.util.List;

class Spoon {
    boolean isPicked = false;

    public synchronized boolean pickSpoon() {
        if (isPicked == true) {
//            System.out.println("cant pick");
            return false;
        } else {
            isPicked = true;
            return isPicked;
        }
    }

    public synchronized void leaveSpoon() {
        if (isPicked == false) {
            System.out.println("already on table");

        } else {
            isPicked = false;

        }
    }
}

enum State {
    THINKING,
    EATING
}

class Philospher {
    State state;
    Integer id;
    Spoon leftSpoon;
    Spoon rightSpoon;

    Philospher(Integer id, Spoon left, Spoon right) {
        this.id = id;
        this.leftSpoon = left;
        this.rightSpoon = right;
    }

    public boolean tryToEat() {
        boolean wasleftPicked = leftSpoon.pickSpoon();
        boolean wasrightPicked = rightSpoon.pickSpoon();
        if (wasleftPicked && wasrightPicked) {
            state = State.EATING;
            System.out.println("philospher " + id + " eating");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            leftSpoon.leaveSpoon();
            rightSpoon.leaveSpoon();
            System.out.println("philospher " + id + " finished eating");
            return true;
        }else if(wasrightPicked){
            rightSpoon.leaveSpoon();
        }else if(wasleftPicked){
            leftSpoon.leaveSpoon();
        }else{
            return false;
        }
        return false;
    }

    public void contemplating() {
        state = State.THINKING;
        try {
            System.out.println(Thread.currentThread().getName() + " philospher = " + id + " contemplating");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class diningphilosphers {
    static List<Philospher> philosphers = new ArrayList<>();
    static List<Spoon> spoons = new ArrayList<>();
    static List<Integer> stats = new ArrayList<>(5);

    public static void main(String[] args) throws InterruptedException {
        // 0 1 2 3 4
        // 0 1 2 3 4
        int runCount = 1000;
        for (int i = 0; i < 5; i++) {
            stats.add(i, 0);
        }

        for (int i = 0; i < 5; i++) {
            spoons.add(new Spoon());
        }
        for (int i = 0; i < 5; i++) {
            philosphers.add(new Philospher(i, spoons.get(i), spoons.get((i + 1) % 5)));
        }
        Thread[] pool = new Thread[5];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            pool[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " :: philospher no =" + finalI + "..");
                Philospher p = philosphers.get(finalI);
                int run = 0;
                while (run < runCount) {
//                    System.out.println(" philospher " + finalI + " run =" + run);
                    p.contemplating();
                    while (!p.tryToEat()) {
                        System.out.println(Thread.currentThread().getName() + " philospher no =" + finalI + " was not able to eat.");
                        p.contemplating();
                    }
//                    System.out.println(Thread.currentThread().getName() + " philospher no =" + finalI + " ate to my hearts content");
                    Integer eatCount = stats.get(finalI);
                    stats.set(finalI, eatCount + 1);
                    run++;
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            pool[i].start();
        }
        for (int i = 0; i < 5; i++) {
            pool[i].join();
        }
        System.out.println("stat:: ");
        stats.forEach(System.out::println);
    }
}
