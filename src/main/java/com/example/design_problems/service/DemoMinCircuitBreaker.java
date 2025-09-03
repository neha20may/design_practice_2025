package com.example.design_problems.service;

import java.util.Random;
import java.util.concurrent.*;

// --------- 1) MINIMAL CIRCUIT BREAKER (with logs) ----------
class MiniCircuitBreaker {
    enum State { CLOSED, OPEN, HALF_OPEN }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private final int failureThreshold;
    private final long openTimeMs;
    private long lastOpenedAt = 0;

    MiniCircuitBreaker(int failureThreshold, long openTimeMs) {
        this.failureThreshold = failureThreshold;
        this.openTimeMs = openTimeMs;
        log("INIT  -> state=CLOSED, threshold=" + failureThreshold + ", openTimeMs=" + openTimeMs);
    }

    public <T> T call(Callable<T> action, T fallback) {
        long now = System.currentTimeMillis();

        if (state == State.OPEN) {
            long remaining = openTimeMs - (now - lastOpenedAt);
            if (remaining > 0) {
                log("OPEN  -> short-circuit (cooldown " + remaining + "ms left)");
                return fallback;
            } else {
                transitionTo(State.HALF_OPEN, "cooldown elapsed");
            }
        }

        try {
            T result = action.call();
            if (failureCount > 0) log("CLOSED-> success resets failureCount (" + failureCount + " -> 0)");
            failureCount = 0;
            if (state != State.CLOSED) transitionTo(State.CLOSED, "trial success");
            return result;
        } catch (Exception e) {
            failureCount++;
            log("CALL  -> failure (" + e.getClass().getSimpleName() + "), failureCount=" + failureCount);
            if (state == State.HALF_OPEN || failureCount >= failureThreshold) {
                lastOpenedAt = now;
                transitionTo(State.OPEN, (state == State.HALF_OPEN ? "trial failed" : "threshold reached"));
            }
            return fallback;
        }
    }

    public State getState() { return state; }

    private void transitionTo(State newState, String reason) {
        log("STATE -> " + state + " -> " + newState + " (" + reason + ")");
        state = newState;
    }

    private static void log(String msg) {
        System.out.printf("[%tT.%<tL] %s%n", System.currentTimeMillis(), msg);
    }
}

// --------- 2) TINY TIMEOUT RUNNER ----------
class TimeoutRunner {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static <T> T runWithTimeout(Callable<T> task, long timeoutMs) throws Exception {
        Future<T> f = pool.submit(task);
        try {
            return f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            f.cancel(true);
            throw te;
        } catch (ExecutionException ee) {
            Throwable cause = ee.getCause();
            if (cause instanceof Exception) throw (Exception) cause;
            throw new RuntimeException(cause);
        }
    }

    public static void shutdown() { pool.shutdownNow(); }
}

// --------- 3) DEMO ----------
public class DemoMinCircuitBreaker {
    private String timeoutTask() throws InterruptedException {
        Random rnd = new Random();
        double x = rnd.nextDouble();
        if (x < 0.35) {                           // ~35% hard failure
            throw new RuntimeException("Remote 5xx/IO");
        }
        if (x < 0.35 + 0.40) {                    // ~40% slow call
            Thread.sleep(1200);                   // exceeds timeout => TimeoutException
        }
        return "SUCCESS@" + System.nanoTime();

    }
    private String circuitBreakerTask() throws Exception {
        long timeoutMs = 800;
        return TimeoutRunner.runWithTimeout(this::timeoutTask, timeoutMs);
    }
    public static void main(String[] args) throws Exception {
        MiniCircuitBreaker cb = new MiniCircuitBreaker(
                3,      // 3 failures → OPEN
                2000    // stay OPEN for 2s
        );

        Random rnd = new Random();
        long timeoutMs = 800;
        DemoMinCircuitBreaker demoObj= new DemoMinCircuitBreaker();
        for (int i = 1; i <= 18; i++) {
            String result = cb.call(
                    // protected call (may fail or be slow)
                    demoObj::circuitBreakerTask,
                    // fallback
                    "FALLBACK"
            );

            System.out.printf("call=%02d  state=%-9s  result=%s%n", i, cb.getState(), result);
            Thread.sleep(400);
        }

        TimeoutRunner.shutdown();
    }
}

