package com.example.design_problems.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.min;

/**
 * Imagine you have a bucket that gets filled with tokens at the rate of 1 token per second. The bucket can hold a maximum of N tokens.
 * Implement a thread-safe class that lets threads get a token when one is available. If no token is available, then the token-requesting threads should block.
 * The class should expose an API called getToken that various threads can call to get a token.
 */

class intelligentBucket {
    int N = 3;
    int availableTokens = 0;
    long lastRefillTime = System.currentTimeMillis();
    long intervalMs = 1000;
    int capacity = N;
//
//    public synchronized void getToken() {
//        long currenTime = System.currentTimeMillis();
//        possibleTokens = (int) ((currenTime - lastQueriedTime) / 5000);
//        if (possibleTokens > N) {
//            possibleTokens = N;
//        }
//        while ((int) ((System.currentTimeMillis() - lastQueriedTime) / 5000) == 0) { //if just this condition to wait then this thread is waiting all his life as there is no producer to wake it up
//            try {
//                System.out.println(Thread.currentThread().getName() + " possible tokens 0 so waiting now. current time ="+System.currentTimeMillis()+" last queried time="+lastQueriedTime);

    /// /                wait(); //if just this condition to wait then this thread is waiting all his life as there is no producer to wake it up
//                Thread.sleep(5000);//so just wait the right time
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println(Thread.currentThread().getName()+" cool, i am inside this method at this moment and possible tokens are " + (int) ((System.currentTimeMillis() - lastQueriedTime) / 5000) + "so i am goood, exiting now after updating tokens and last queried time");
//        possibleTokens--;
//        lastQueriedTime = System.currentTimeMillis();
//    }
    private long fill() {

        long now = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " trying to refill at " + now);
        long elapsed = now - lastRefillTime;
        int tokensToAdd = (int) (elapsed / intervalMs);
        System.out.println(Thread.currentThread().getName() + " tokens to add=" + tokensToAdd);
        if (tokensToAdd > 0) {
            availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
            lastRefillTime = lastRefillTime + tokensToAdd * intervalMs;
            //lastRefillTime = lastRefillTime + (int) (elapsed / intervalMs) * intervalMs;
            //lastRefillTime = lastRefillTime + (int) (now - lastRefillTime)
            //lastRefillTime = now
            System.out.println(Thread.currentThread().getName() + " available tokens = " + availableTokens + " last refill time=" + lastRefillTime);
        }
        return now;
    }

    public synchronized void getToken() {
        while (true) {
            long now = fill();
            if (availableTokens > 0) {
                System.out.println(Thread.currentThread().getName() + " got my token ; returning ");
                availableTokens--;
                break;
            } else {
                long remTimeForNexToken = intervalMs - (now - lastRefillTime);
                if (remTimeForNexToken < 0) {
                    remTimeForNexToken = 1; //some say wait(0) is indefinite
                }
                System.out.println(Thread.currentThread().getName() + " need to wait for remaining time=" + remTimeForNexToken);
                try {
                    wait(remTimeForNexToken);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}

class conbucket2 {
    int N = 3;
    //    Thread[] pool;
//    ExecutorService pool = Executors.newFixedThreadPool(N);
    ExecutorService pool;
    intelligentBucket bucket = new intelligentBucket();
    List<Runnable> tasks= new ArrayList<>();

    public conbucket2(int n) {
        this.N = n;
        this.pool = Executors.newFixedThreadPool(N);
    }

    private void createpool() {
        System.out.println("creating pool with N=" + N);
        for (int i = 0; i < N; i++) {
//            pool.submit(
////                    new Thread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            bucket.getToken();
//                        }
////                        )
//                    });
////            pool[i].setName("pool thread " + i);
//            tasks.add(new Runnable() {
//                @Override
//                public void run() {
//                    bucket.getToken();
//                }
//            });

            tasks.add(()->{bucket.getToken();});

        }

    }

    public void runpool() {
        createpool();
//        for (int i = 0; i < N; i++) {
//            pool[i].start();
//        }
        for(Runnable task: tasks){
            pool.submit(task);
        }

    }
//
//    public void waitpool() {
//        for (int i = 0; i < N; i++) {
//            try {
//                pool[i].join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
    public void stoppool(){
        pool.shutdown();
    }
}

public class ratelimiterbucket2 {
    public static void main(String[] args) {
        //no need to write producer
        conbucket2 cobj = new conbucket2(100);
        cobj.runpool();
//        cobj.waitpool();
        cobj.stoppool();

    }
}
