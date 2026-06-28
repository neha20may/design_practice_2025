package com.example.design_problems.service;

import java.util.concurrent.Callable;

class VerySimpleCircuitBreaker {
    enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private final int failureThreshold;
    private final long openDurationMs;
    private long openedAt = 0;

    VerySimpleCircuitBreaker(int failureThreshold, long openDurationMs) {
        this.failureThreshold = failureThreshold;
        this.openDurationMs = openDurationMs;
    }

    public <T> T call(Callable<T> riskyCall, T fallback) {
        if (state == State.OPEN) {
            boolean cooldownFinished = System.currentTimeMillis() - openedAt >= openDurationMs;
            if (!cooldownFinished) {
                System.out.println("Circuit OPEN: skipping risky call");
                return fallback;
            }

            state = State.HALF_OPEN;
            System.out.println("Circuit HALF_OPEN: trying one test call");
        }

        try {
            T result = riskyCall.call();
            failureCount = 0;
            state = State.CLOSED;
            System.out.println("Call success: circuit CLOSED");
            return result;
        } catch (Exception e) {
            failureCount++;
            System.out.println("Call failed: failureCount=" + failureCount);

            if (state == State.HALF_OPEN || failureCount >= failureThreshold) {
                state = State.OPEN;
                openedAt = System.currentTimeMillis();
                System.out.println("Circuit OPENED");
            }

            return fallback;
        }
    }

    public State getState() {
        return state;
    }
}

class VeryUnstableService {
    private int callNumber = 0;

    public String fetchData() {
        callNumber++;

        if (callNumber >= 2 && callNumber <= 4) {
            throw new RuntimeException("Remote service failed");
        }

        return "DATA_FROM_REMOTE_CALL_" + callNumber;
    }
}

public class DemoVerySimpleCircuitBreaker {
    public static void main(String[] args) throws InterruptedException {
        VerySimpleCircuitBreaker circuitBreaker = new VerySimpleCircuitBreaker(
                3,      // after 3 failures, open the circuit
                1500    // stay open for 1.5 seconds
        );

        VeryUnstableService service = new VeryUnstableService();

        for (int i = 1; i <= 10; i++) {
            String result = circuitBreaker.call(
                    service::fetchData,
                    "FALLBACK_DATA"
            );

            System.out.println("request=" + i
                    + ", state=" + circuitBreaker.getState()
                    + ", result=" + result);
            System.out.println();

            Thread.sleep(400);
        }
    }
}
