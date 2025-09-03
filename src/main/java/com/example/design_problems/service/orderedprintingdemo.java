package com.example.design_problems.service;

import java.util.concurrent.Semaphore;

class OrderedPrinting {

    public void printFirst() {
        System.out.println("First");
    }

    public void printSecond() {
        System.out.println("Second");
    }

    public void printThird() {
        System.out.println("Third");
    }

}

public class orderedprintingdemo {
    public static void main(String[] args) {
        OrderedPrinting obj = new OrderedPrinting();

        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(1);
        Thread t1 = new Thread(() -> {
            obj.printFirst();
            s1.release();

        });
        Thread t2 = new Thread(() -> {
            while (s1.tryAcquire()) {
                try {
                    s1.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            obj.printSecond();
            s2.release();
        });
        Thread t3 = new Thread(() -> {
            while (s2.tryAcquire()) {
                try {
                    s2.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            obj.printThird();
        });
        //order the threads
        s1.tryAcquire();
        s2.tryAcquire();
        t1.start();
        t2.start();
        try {
            t3.start();
            t1.join();
            t2.join();
            t2.join();
        } catch (Exception e) {
        }
        System.out.println("main exiting");

    }
}
