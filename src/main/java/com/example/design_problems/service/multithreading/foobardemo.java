package com.example.design_problems.service.multithreading;

class FooBar {
    private int n;
    private int flag = 0;

    public FooBar(int n, int flag) {
        this.n = n;
        this.flag= flag;
    }

    //    public synchronized void foo() {// this is so wrong
    public void foo() {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                while (flag == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("foo");
                flag = 0;
                notifyAll();
            }
        }
    }

    //    public synchronized void bar() {// this is so wrong
    public void bar() {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                while (flag == 1) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                System.out.println("bar");
                flag = 1;
                notifyAll();
            }
        }
    }
}

public class foobardemo {


    public static void main(String[] args) {
        FooBar obj = new FooBar(10, 1);
        Thread t1 = new Thread(() -> {
            obj.foo();

        });
        Thread t2 = new Thread(() -> {
            obj.bar();
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (Exception e) {

        }
    }

}
