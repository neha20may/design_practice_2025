# AGENTS.md

## Repository Purpose

This is a practice repository for low-level design problems, Java concurrency exercises, and small service-design demos. Treat the code as learning-oriented examples: prioritize clarity, runnable demonstrations, and explicit tradeoffs over production-level abstractions.

## Collaboration Style

Use a teacher-style approach in this repository. When discussing or changing solutions, explain the idea first, then the code mechanics, then the edge cases and common interview traps. Prefer guiding questions, small examples, and step-by-step reasoning over terse answers.

For revisions, help connect the current implementation to the underlying concept so the solution is easier to recall later. If a better primitive or design exists, explain why it fits before changing the code.

## Project Layout

- `src/main/java/com/example/design_problems/service`: service-design examples such as circuit breakers, rate limiters, order service demos, and Splitwise-style balance logic.
- `src/main/java/com/example/design_problems/service/multithreading`: concurrency exercises such as async-to-sync coordination, ordered printing, pub/sub, dining philosophers, and monitor/semaphore examples.
- `src/main/java/com/example/design_problems/controller`: Spring controller examples.
- `src/test/java`: Spring Boot test scaffold and any focused tests added for exercises.

## Tech Stack

- Java 8
- Maven wrapper via `./mvnw`
- Spring Boot 2.7.x
- Some examples are standalone `main` methods and do not need the Spring app to run.

## Common Commands

```bash
./mvnw test
./mvnw spring-boot:run
./mvnw -DskipTests package
```

To run an individual standalone Java exercise, prefer using the IDE run configuration for the class with a `main` method. If adding tests around a specific design problem, keep them narrow and deterministic.

## Coding Guidelines

- Keep each design problem self-contained unless sharing code clearly improves the exercise.
- Prefer readable, interview-style implementations with comments only where they explain non-obvious concurrency or design choices.
- Use Java concurrency primitives intentionally: explain why a `Semaphore`, `CountDownLatch`, `synchronized`, `Lock`, `BlockingQueue`, or `ExecutorService` is appropriate for the problem.
- Avoid busy waiting when a blocking primitive would express the solution more directly.
- Preserve Java 8 compatibility.
- Prefer conventional Java naming for new code: `AsyncToSync`, `RateLimiterBucket`, `DiningPhilosophers`, etc. Existing practice files may use lowercase names; do not rename them unless the task explicitly asks for cleanup.
- Keep examples runnable from a `main` method when the goal is demonstration.
- For Spring service examples, keep controller and service responsibilities separate.

## LLD Practice Expectations

When adding or modifying a low-level design solution:

1. State the core entities and responsibilities in code structure.
2. Keep APIs small and explicit.
3. Call out concurrency assumptions when shared state is involved.
4. Include a simple demo or focused test that shows the behavior.
5. Avoid over-engineering unless the exercise specifically asks for extensibility.

## Notes For `multithreading/asynctosync.java`

This file demonstrates converting asynchronous callback completion into synchronous waiting. If revisiting it, consider using `CountDownLatch` for a one-time completion signal, or keep `Semaphore` only if the exercise is specifically about permits. Make sure the main thread cannot miss the completion signal and does not spin unnecessarily.

## Dependency Guidance

Do not add external libraries for small interview-style exercises unless the problem is specifically about integrating a framework or comparing library behavior. Prefer JDK collections and concurrency utilities for core LLD practice.
