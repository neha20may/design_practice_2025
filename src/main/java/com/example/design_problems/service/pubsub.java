package com.example.design_problems.service;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class resource {
    int N = 3;
    Queue<Integer> queue = new ArrayDeque<>(N);

    public synchronized void produce(producer producer) {
        while (queue.size() == N) {
            try {
//                synchronized (this){
                wait();
//                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        queue.add(1);
        System.out.println("item added by " + Thread.currentThread().getName());
        notify();
    }

    void consume(consumer consumer) {
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
//                synchronized (this){
                    wait();
//                }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.poll();
            System.out.println("item removed by thread " + Thread.currentThread().getName());
//            this.notify();//wakes up one thread - ti could be producer too
            //notify all wakes up all -producers and consumers
            this.notifyAll();
        }
    }
}

class producer implements Runnable {
    resource r;

    public producer(resource r) {
        this.r = r;
    }

    @Override
    public void run() {
        int n = 1000;
        int i = 0;
        while (i < n) {
            r.produce(this);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
    }
}

class consumer implements Runnable {
    resource r;

    public consumer(resource r) {
        this.r = r;
    }

    @Override
    public void run() {
        int i = 0;
        int n = 1000;
        while (i < n) {
            r.consume(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
    }
}

class pubsub {
    public static void main(String[] args) {
        resource r = new resource();
        producer p = new producer(r);
        consumer c = new consumer(r);
//        Thread producer = new Thread(p);
//        Thread consumer = new Thread(c);
//        producer.start();
//        consumer.start();

        ExecutorService pool= Executors.newFixedThreadPool(6);
        for(int i=0;i<3; i++){
            pool.submit(new producer(r));
        }
        for(int i=0;i<2; i++){
            pool.submit(new consumer(r));
        }

    }

}
