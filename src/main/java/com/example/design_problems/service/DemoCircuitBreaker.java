package com.example.design_problems.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** --------------- CIRCUIT BREAKER (no frameworks) --------------- */
final class SimpleCircuitBreaker {

    enum State { CLOSED, OPEN, HALF_OPEN }

    // --- Tunables
    private final int minCalls;                    // size of sliding window
    private final double failureRateThreshold;     // e.g., 0.5 = 50%
    private final long openIntervalMillis;         // how long to stay OPEN
    private final int halfOpenPermits;             // trial calls allowed in HALF_OPEN

    // --- Runtime
    private final Deque<Boolean> window = new ArrayDeque<>();
    private volatile State state = State.CLOSED;
    private volatile long openedAt = 0L;
    private final AtomicInteger halfOpenInFlight = new AtomicInteger(0);

    SimpleCircuitBreaker(int minCalls,
                         double failureRateThreshold,
                         long openIntervalMillis,
                         int halfOpenPermits) {
        this.minCalls = minCalls;
        this.failureRateThreshold = failureRateThreshold;
        this.openIntervalMillis = openIntervalMillis;
        this.halfOpenPermits = halfOpenPermits;
    }

    /** Main entry: wraps a call with CB + fallback. */
    public <T> T call(Callable<T> fn, Supplier<T> fallback) throws Exception {
        long now = System.currentTimeMillis();

        // Fast path decisions by state
        if (state == State.OPEN) {
            if (now - openedAt < openIntervalMillis) {
                return fallback.get(); // short-circuit while OPEN window not elapsed
            }
            transitionToHalfOpen();
        }

        if (state == State.HALF_OPEN) {
            if (halfOpenInFlight.incrementAndGet() > halfOpenPermits) {
                halfOpenInFlight.decrementAndGet();
                return fallback.get(); // too many trial calls → bail out
            }
            try {
                T res = executeAndRecord(fn);
                // simple rule: if last minCalls are all success → CLOSE
                if (allRecentSuccess()) {
                    transitionToClosed();
                }
                return res;
            } catch (Exception e) {
                record(false);
                tripOpen();
                throw e;
            } finally {
                halfOpenInFlight.decrementAndGet();
            }
        }

        // CLOSED
        try {
            return executeAndRecord(fn);
        } catch (Exception e) {
            record(false);
            if (shouldOpen()) tripOpen();
            throw e;
        }
    }

    /** Executes fn, records success, and returns result. */
    private <T> T executeAndRecord(Callable<T> fn) throws Exception {
        try {
            T res = fn.call();
            record(true);
            return res;
        } catch (Exception e) {
            record(false);
            throw e;
        }
    }

    /** Add outcome into sliding window. */
    private synchronized void record(boolean success) {
        window.addLast(success);
        if (window.size() > minCalls) window.removeFirst();
    }

    /** Check failure rate on the last minCalls outcomes. */
    private synchronized boolean shouldOpen() {
        if (window.size() < minCalls) return false;
        long failures = window.stream().filter(b -> !b).count();
        double rate = (double) failures / window.size();
        return rate >= failureRateThreshold;
    }

    /** In HALF_OPEN, close only if all recent are success (simple rule). */
    private synchronized boolean allRecentSuccess() {
        if (window.size() < minCalls) return false;
        for (Boolean b : window) if (!b) return false;
        return true;
    }

    private void tripOpen() {
        state = State.OPEN;
        openedAt = System.currentTimeMillis();
    }

    private void transitionToHalfOpen() {
        state = State.HALF_OPEN;
        halfOpenInFlight.set(0);
    }

    private void transitionToClosed() {
        state = State.CLOSED;
        // keep window; you could also clear() to forget the past
    }

    public State state() { return state; }
}

/** --------------- TIMEOUT WRAPPER (counts slow calls as failures) --------------- */
final class Timeouts {
    private final ExecutorService pool;

    Timeouts(int threads) {
        this.pool = Executors.newFixedThreadPool(threads);
    }

    public <T> T runWithTimeout(Callable<T> task, long timeoutMs) throws Exception {
        Future<T> f = pool.submit(task);
        try {
            return f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            f.cancel(true);                 // interrupt if running
            throw te;                       // bubble up as failure
        } catch (ExecutionException ee) {
            throw (ee.getCause() instanceof Exception)
                    ? (Exception) ee.getCause()
                    : new RuntimeException(ee.getCause());
        }
    }

    public void shutdown() { pool.shutdownNow(); }
}

/** --------------- A FLAKY DEPENDENCY TO CALL --------------- */
final class UnreliableApi {
    private final Random rnd = new Random();
    private final double failProb;  // e.g., 0.35 → 35% exceptions
    private final double slowProb;  // e.g., 0.30 → 30% slow calls
    private final long slowMs;      // how slow

    UnreliableApi(double failProb, double slowProb, long slowMs) {
        this.failProb = failProb;
        this.slowProb = slowProb;
        this.slowMs = slowMs;
    }

    public String fetchData() throws Exception {
        double x = rnd.nextDouble();

        if (x < failProb) {
            throw new RuntimeException("Remote 5xx / IO error");
        }
        if (x < failProb + slowProb) {
            Thread.sleep(slowMs); // simulate slowness
        }
        return "OK:" + System.nanoTime();
    }
}

/** --------------- DEMO / USAGE --------------- */
public class DemoCircuitBreaker {
    public static void main(String[] args) throws Exception {
        // 1) construct pieces
        SimpleCircuitBreaker cb = new SimpleCircuitBreaker(
                10,         // minCalls: window size
                0.5,        // failureRateThreshold: 50%
                3000,       // openIntervalMillis: 3s
                2           // halfOpenPermits: allow 2 trial calls at a time
        );
        Timeouts timeouts = new Timeouts(4);
        UnreliableApi api = new UnreliableApi(
                0.35,   // 35% chance of throwing
                0.30,   // 30% chance of being slow
                1200    // slow means >1.2s
        );

        // 2) a small wrapper that combines CB + timeout + fallback
        Supplier<String> fallback = () -> "FALLBACK_STALE_DATA";
        Callable<String> protectedCall = () -> timeouts.runWithTimeout(
                api::fetchData,
                800 // timeoutMs (anything slower than 800ms counts as failure)
        );

        // 3) run a small load and print what happens
        for (int i = 1; i <= 40; i++) {
            try {
                String result = cb.call(protectedCall, fallback);
                System.out.printf("%02d  %-6s  %s%n", i, cb.state(), result);
            } catch (Exception e) {
                System.out.printf("%02d  %-6s  EX: %s%n", i, cb.state(), e.getClass().getSimpleName());
            }
            Thread.sleep(150); // small pacing so you can watch transitions
        }

        timeouts.shutdown();
    }
}
