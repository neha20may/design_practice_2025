package com.example.design_problems.service.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * A barbershop consists of a waiting room with n chairs, and a barber chair for giving haircuts.
 * If there are no customers to be served, the barber goes to sleep.
 * If a customer enters the barbershop and all chairs are occupied, then the customer leaves the shop.
 * If the barber is busy, but chairs are available, then the customer sits in one of the free chairs.
 * If the barber is asleep, the customer wakes up the barber.
 * Write a program to coordinate the interaction between the barber and the customers.
 */

class Shop {
    Semaphore chairs = new Semaphore(5);

    public void enterShop(Customer customer) {

        boolean acquired = chairs.tryAcquire(); //thread waits if no chair- ideally customer should leave the shop not wait

        if (acquired) {

            synchronized (this) {
                System.out.println("customer " + customer.id + " acquired chair; ");
                System.out.println("customer " + customer.id + " gonna notify barber");
                notifyAll();
            }
        } else {
            System.out.println("customer " + customer.id + " no vacant chair, exiting shop");
        }

    }

    public void exitShop() {
        chairs.release();
    }
}

class Barber implements Runnable {
    Shop shop;

    Barber(Shop shop) {
        this.shop = shop;
    }

    public void cut() {
        System.out.println("Barber cutitng..");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        shop.exitShop();
        System.out.println("Barber cut over !! Khich Khich!!");
    }

    public void run() {
        while (true) {
            synchronized (shop) { //did wrong here first time-  if there is gap between this and wait; there is chance of missed notify; !!between lines things happen in multithreading!!
                while (shop.chairs.availablePermits() == 5) {
                    try {
                        System.out.println("Barber - no customer barber going to sleep");
                        shop.wait();//mimick sleep
                        System.out.println("Barber woke availbale permmits= " + shop.chairs.availablePermits());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            cut();
        }
    }
}

class Customer implements Runnable {
    public String id;
    Shop shop;

    Customer(String id, Shop shop) {
        this.id = id;
        this.shop = shop;
    }

    public void run() {
        System.out.println("Customer id = " + id + " entering in shop");
        shop.enterShop(this);
    }

}

public class barber_shop {

    public static void main(String[] args) throws InterruptedException {

        List<Customer> customers = new ArrayList<>();
        Shop shop = new Shop();
        Barber barber = new Barber(shop);
        for (int i = 0; i < 10; i++) {
            customers.add(new Customer(i + "", shop));
        }
        Thread barberT = new Thread(barber);
        Thread[] customerT = new Thread[10];
        for (int i = 0; i < 10; i++) {
            customerT[i] = new Thread(customers.get(i));
        }
        barberT.start();
        Thread.sleep(1000);

        for (int i = 0; i < 5; i++) {
            customerT[i].start();
        }

        for (int i = 0; i < 5; i++) {
            customerT[i].join();
        }
        Thread.sleep(1000);
        for (int i = 5; i < 10; i++) {
            customerT[i].start();
        }

        for (int i = 5; i < 10; i++) {
            customerT[i].join();
        }

        Thread.sleep(2000);
        Thread t = new Thread(new Customer("101", shop));
        t.start();
        t.join();

        Thread.sleep(1000);
        Thread t1 = new Thread(new Customer("102", shop));
        t1.start();
        t1.join();

    }

}
