# Circuit Breaker

## The Simple Idea

A circuit breaker is a failure protection pattern.

Imagine an electrical circuit breaker in a house:

```text
current too high -> breaker trips -> electricity stops
```

Software uses the same idea when one service calls another service.

Example:

```text
Order service -> Payment service
```

If the payment service is healthy, call it normally.

If the payment service is down or very slow, repeatedly calling it is harmful:

```text
request 1 -> calls payment -> waits -> fails
request 2 -> calls payment -> waits -> fails
request 3 -> calls payment -> waits -> fails
request 4 -> calls payment -> waits -> fails
```

Now the order service also becomes slow because it keeps waiting for a bad dependency.

A circuit breaker says:

```text
This dependency is failing too much.
Stop calling it for some time.
Return fallback immediately.
```

## What Problem It Solves

Without a circuit breaker:

```text
Every request keeps trying the broken dependency.
Every request waits.
Threads get blocked.
Latency increases.
The whole service can become unhealthy.
```

With a circuit breaker:

```text
Try the dependency while it looks healthy.
If it fails too much, stop calling it.
Return fallback quickly.
After a cooldown, test if it recovered.
```

So the circuit breaker answers this question:

```text
Should I even call this unhealthy dependency right now?
```

## The Three States

### CLOSED

Normal state.

```text
Calls are allowed.
Failures are counted.
```

Think:

```text
Circuit is closed, so traffic flows.
```

### OPEN

Broken/protection state.

```text
Calls are blocked.
Fallback is returned immediately.
```

Think:

```text
Circuit is open, so traffic does not flow.
```

### HALF_OPEN

Trial state.

```text
Cooldown is over.
Allow one or a few test calls.
If test succeeds, go back to CLOSED.
If test fails, go back to OPEN.
```

Think:

```text
Maybe the dependency recovered. Test carefully.
```

## Example Flow

```text
Start in CLOSED

call 1 -> payment fails
call 2 -> payment fails
call 3 -> payment fails

failure threshold reached

move to OPEN

call 4 -> do not call payment, return fallback
call 5 -> do not call payment, return fallback
call 6 -> do not call payment, return fallback

cooldown time passes

move to HALF_OPEN

trial call -> success

move to CLOSED again
```

If the trial call fails:

```text
HALF_OPEN -> OPEN
```

## Why Timeout Matters

A dependency may not always throw an exception. Sometimes it becomes slow.

That is also a failure for your service.

Example:

```text
Payment service responds after 10 seconds.
Your service only wants to wait 800 ms.
```

So a circuit breaker is usually combined with timeout handling:

```text
exception -> failure
timeout -> failure
too many failures -> OPEN
```

## Mapping To This Repository

This repo has two circuit breaker demos:

```text
src/main/java/com/example/design_problems/service/DemoMinCircuitBreaker.java
src/main/java/com/example/design_problems/service/DemoCircuitBreaker.java
```

Use `DemoMinCircuitBreaker.java` first for revision.

It has:

```java
enum State { CLOSED, OPEN, HALF_OPEN }
```

It opens after a simple number of failures:

```text
3 failures -> OPEN
stay OPEN for 2 seconds
after 2 seconds -> HALF_OPEN
success -> CLOSED
failure -> OPEN
```

The important method is:

```java
public <T> T call(Callable<T> action, T fallback)
```

Meaning:

```text
Try this risky action.
If circuit is OPEN, return fallback.
If action fails too much, OPEN the circuit.
```

`DemoCircuitBreaker.java` is the more production-like version.

Instead of opening after a fixed number of failures, it uses a sliding window:

```text
Look at recent calls.
If failure rate is too high, OPEN.
```

Example:

```text
last 10 calls
5 or more failed
failure rate >= 50%
OPEN
```

## Memory Hook

```text
CLOSED    = normal calls allowed
OPEN      = dependency looks broken, return fallback fast
HALF_OPEN = test if dependency recovered
```

The circuit breaker protects your service from repeatedly waiting on something already known to be unhealthy.
