package com.example.design_problems.service.multithreading;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Design and implement a thread-safe class that allows registeration of callback methods that are executed after a user specified time interval in seconds has elapsed.
 */
class Task {
    Runnable r;
    long time;

    public Task(Runnable r, long t) {
        this.r = r;
        this.time = t;
    }

    public String toString() {
        return new StringBuilder("Task at time =" + time).toString();
    }
}

class TaskHolder {
    Queue<Task> pq = new PriorityQueue<>(Comparator.comparing(t -> t.time, (a, b) -> {
        return Long.compare(a, b);
    }));

    public synchronized void add(Task t) {
        pq.add(t);
        notifyAll();
    }

    public synchronized void runTask() {
        Task task = pq.poll();
        if (task != null) {
            Thread t = new Thread(task.r);
            t.start();
        }
    }
}

class Executioner implements Runnable {
    TaskHolder taskHolder;

    Executioner(TaskHolder taskHolder) {
        this.taskHolder = taskHolder;
    }

    @Override
    public void run() {
        while (true) {
            Runnable torun = null;
            synchronized (taskHolder) { //loack the same object as on adding in to tehe queue
                while (taskHolder.pq.isEmpty()) { //always recheck in wait and notify
                    try {
                        taskHolder.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                long now = System.currentTimeMillis();
                long latestTask = taskHolder.pq.peek().time;
                long delay = latestTask - now;
                System.out.println("Executioner checking the peek of pq headtime=" + taskHolder.pq.peek().time);
                if (delay > 0) {
                    try {
                        System.out.println("gonna wait for delay =" + delay / 1000 + " sec");
                        taskHolder.wait(delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

                Task due = taskHolder.pq.poll();
                if (due != null) {
                    torun = due.r;
                    System.out.println("out of wait, due task is " + due);
                }
            }
//            torun.run(); //this runs on this executoner thread
            new Thread(torun).start(); //while this spawns a new thread
        }
    }
}


public class threadSafecallback {
    static Task[] list = new Task[10];

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            long time = System.currentTimeMillis() + 2000;
            int finalI = i;
            list[i] = new Task(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " :: Task" + finalI + " running at  " + System.currentTimeMillis());
                }
            }, time + 1000 * i);
        }
        TaskHolder taskHolder = new TaskHolder();
        for (int i = 0; i < 10; i++) {
            taskHolder.add(list[i]);
        }
        Thread runnerThread = new Thread(new Executioner(taskHolder));
        runnerThread.start();
//        runnerThread.join();
        Task urgentTask = new Task(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " running urgent task at " + System.currentTimeMillis());
            }
        }, System.currentTimeMillis());
        taskHolder.add(urgentTask);

    }
}
