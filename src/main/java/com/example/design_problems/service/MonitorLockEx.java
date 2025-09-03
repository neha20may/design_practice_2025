package com.example.design_problems.service;

import org.springframework.ui.context.Theme;

class MonitorLockExample {
    Integer otherObj= new Integer(2);

    public synchronized void task1() {
        System.out.println("task 1");
        try {
            Thread.sleep(3000);
            System.out.println("task1 finish");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void task2() {
        System.out.println("before sync task 2");
        synchronized (this) {
            System.out.println("in sync block task 2");

        }
    }

    public void task3() {
        System.out.println("task 3");
    }
    //task 4 unrelated to sync on other ob so same as task 3 and task 4
    public void task4(){
        synchronized (otherObj) {
            System.out.println("task 4");
        }
    }
    public void task5(){
        System.out.println("before sync task 5");
        synchronized (otherObj){
            System.out.println("after sync task 5");
        }
    }
}
class monitorLockEx{

    public static void main(String[] args) {
        MonitorLockExample obj= new MonitorLockExample();
        //monotr lock is on same object
        Thread t1= new Thread(()-> obj.task1());
        Thread t2= new Thread(()-> obj.task2());
        Thread t3= new Thread(()->obj.task3());
        Thread t4= new Thread(()->obj.task4());
        Thread t5= new Thread(()->obj.task5());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        }catch (Exception e){

        }
        System.out.println("main finish");
    }
}
