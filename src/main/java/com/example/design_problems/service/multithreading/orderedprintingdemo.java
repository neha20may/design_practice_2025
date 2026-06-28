package com.example.design_problems.service.multithreading;

import java.util.concurrent.Semaphore;

class OrderedPrinting {
    //share variables mmake the magic!
    int turn = 1;

    public synchronized void printFirst() {
        while (turn != 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("First");
        turn = 2;
        notifyAll();
    }

    public synchronized void printSecond() {
        while (turn != 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        turn = 3;
        System.out.println("Second");
        notifyAll();
    }

    public synchronized void printThird() {

        while (turn != 3) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Third");
        turn = 1;
        notifyAll();
    }

}

public class orderedprintingdemo {
    //    public static void main(String[] args) {
//        OrderedPrinting obj = new OrderedPrinting();
//
//        Semaphore s1 = new Semaphore(1);
//        Semaphore s2 = new Semaphore(1);
//        Thread t1 = new Thread(() -> {
//            obj.printFirst();
//            s1.release();
//
//        });
//        Thread t2 = new Thread(() -> {
//            while (s1.tryAcquire()) {
//                try {
//                    s1.wait();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            obj.printSecond();
//            s2.release();
//        });
//        Thread t3 = new Thread(() -> {
//            while (s2.tryAcquire()) {
//                try {
//                    s2.wait();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            obj.printThird();
//        });
//        //order the threads
//        s1.tryAcquire();
//        s2.tryAcquire();
//        t1.start();
//        t2.start();
//        try {
//            t3.start();
//            t1.join();
//            t2.join();
//            t2.join();
//        } catch (Exception e) {
//        }
//        System.out.println("main exiting");
//
//    }
    public static void main(String[] args) {
        OrderedPrinting obj= new OrderedPrinting();
        Thread t1= new Thread(()->{obj.printFirst();});
        Thread t2= new Thread(()->{obj.printSecond();});
        Thread t3= new Thread(()->{obj.printThird();});
        t1.start();
        t2.start();
        t3.start();

    }
}
