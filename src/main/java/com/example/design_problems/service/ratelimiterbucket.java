package com.example.design_problems.service;

import java.util.*;

/**
 * . Imagine you have a bucket that gets filled with tokens at the rate of 1 token per second. The bucket can hold a maximum of N tokens. Implement a thread-safe class that lets threads get a token when one is available. If no token is available, then the token-requesting threads should block. The class should expose an API called getToken that various threads can call to get a token.
 */

class Bucket{
    Queue<Integer> bucket= new ArrayDeque<>();
    int N=3;
    public synchronized void fillBucket(){
        while(bucket.size()==N){
            try {
                System.out.println(Thread.currentThread().getName() +" bucket full hence going to wait..");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName()+" filled token");
        bucket.add(1);
    }
    public synchronized  Integer getToken(){
        while(bucket.isEmpty()){
            try {
                System.out.println(Thread.currentThread().getName() +" bucket empty hence going to wait..");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName()+"  got token");
        return bucket.poll();
    }
}

class prodBucket implements  Runnable{
    Bucket bucket;

    public prodBucket(Bucket b){
        this.bucket= b;
    }
    @Override
    public void run() {
        int i = 0;
        //assume each thead can fill 1000 times or mimick thread can fill indefnitely
        while (i < 1000){
            bucket.fillBucket();
            i++;
    }
    }
}

class conBucket implements  Runnable{
    Bucket bucket;

    public conBucket(Bucket b){
        this.bucket= b;
    }
    @Override
    public void run() {
        bucket.getToken();
    }
}
public class ratelimiterbucket {
    public static void main(String[] args) {
        Bucket bucket= new Bucket();
        Thread [] producerPool= new Thread[100];
        for(int i=0; i<100; i++){
            producerPool[i]= new Thread(new prodBucket(bucket));
        }
        Thread [] consumerPool = new Thread[100];
        for(int i=0; i<100;i++){
            consumerPool[i]= new Thread(new conBucket(bucket));
        }
        for(int i=0; i<100;i++){
            producerPool[i].start();
            consumerPool[i].start();
        }
        for(int i=0; i<100;i++){
            try {
                producerPool[i].join();
                consumerPool[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("main finish");
    }
}
