package com.example.design_problems.service;


import java.util.concurrent.Semaphore;

class Executor {

    public void asynchronousExecution(Callback callback) throws Exception {

        Thread t = new Thread(() -> {
            // Do some useful work
            try {
                // Simulate useful work by sleeping for 5 seconds
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
            }
            callback.done();
        });
        t.start();
    }
}

interface Callback {

    public void done();
}

class MyObject {
}

public class asynctosync {
    public static void main(String args[]) throws Exception {
        Executor executor = new Executor();
        Semaphore semaphore = new Semaphore(1);
        Callback customCallBack = new Callback() {
            @Override
            public void done() {
                System.out.println("overwritten callback done");
                semaphore.release();

            }
        };
        //case on wait getting blocked if wait here
//        executor.asynchronousExecution(customCallBack);
        //case of missed signal on wait if done after calling the aysnc
//        synchronized (myObject){
//            myObject.wait();
//        }
        boolean isSemAcq = semaphore.tryAcquire();
        executor.asynchronousExecution(customCallBack);
        while (!semaphore.tryAcquire()) {
            System.out.println("main thread shoudl wait , asycn is not done yet");
            Thread.sleep(1000);
        }
        System.out.println("main thread exiting...after it was told by custom callback to do so");


    }
}
