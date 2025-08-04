package com.practice.problems;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@EnableAsync
public class Concurrency {

    public void usingExecutor() {
        ExecutorService execSer = Executors.newFixedThreadPool(3);
        AtomicInteger atomInt = new AtomicInteger(0);
        try {
            execSer.submit(() -> {
                try {
                    Thread.sleep(5000);
                    atomInt.set(111);
                    System.out.println("Thread 1");
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted");
                }

                System.out.println("Thread completed");
            });

            List<Future<Integer>> futures = new ArrayList<>();

            futures.add(execSer.submit(() -> {
                try {
                    Thread.sleep(5000);
                    atomInt.set(222);
                    System.out.println("Thread 2");
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted");
                }
                System.out.println("Thread completed");
                return atomInt.get();
            }));

            futures.add(execSer.submit(() -> {
                try {

                    Thread.sleep(5000);
                    atomInt.set(333);
                    System.out.println("Thread 3");
                } catch (InterruptedException ex) {
                    System.out.println("Thread interrupted");
                }
                System.out.println("Thread completed");
                return atomInt.get();
            }));

            for (Future<Integer> future : futures) {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("Thread interrupted");
                }
            }
        } finally {
            execSer.shutdown();
            try {
                execSer.awaitTermination(15, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                execSer.shutdownNow();
                System.out.println("Thread interrupted");
            }
        }
    }

    @Async
    public ListenableFuture<String> asyncMethodWithFuture() {
        try {
            Thread.sleep(5000);
            return AsyncResult.forValue("Async task completed!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return AsyncResult.forExecutionException(e);
        }
    }

    @Async
    public CompletableFuture<Integer> asyncMethodWithCompletableFuture(int input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return input * 2;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    public void asyncMethodWithVoidReturn(String message) {
        try {
            Thread.sleep(1000);
            System.out.println("Async void method executed with message: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // Example of how to use the async methods
    // Alternative async implementations
    public CompletableFuture<Integer> asyncMethodWithExecutor(int input) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(2000);
                    return input * 2;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }, executor);
        } finally {
            executor.shutdown();
        }
    }

    // Using ForkJoinPool for CPU-intensive tasks
    public CompletableFuture<Integer> asyncMethodWithForkJoinPool(int input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return input * 2;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }, ForkJoinPool.commonPool());
    }

    public CompletableFuture<Integer> asyncMethodWithCustomExecutor(int input) {
        ExecutorService customExecutor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                r -> {
                    Thread t = new Thread(r, "CustomAsync-" + Thread.currentThread().getId());
                    t.setDaemon(true);
                    return t;
                });
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return input * 2;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }, customExecutor);
    }

    public void demonstrateAsyncMethods() throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("Starting async operations...");

        // Fire and forget (void return)
        asyncMethodWithVoidReturn("Hello from async!");

        // ListenableFuture with timeout
        ListenableFuture<String> futureResult = asyncMethodWithFuture();
        futureResult.addCallback(
                result -> System.out.println("Future result: " + result),
                ex -> System.err.println("Error in async execution: " + ex.getMessage()));

        // You can also get the result synchronously if needed
        try {
            String result = futureResult.get(10, TimeUnit.SECONDS);
            System.out.println("Future result (sync): " + result);
        } catch (TimeoutException e) {
            System.err.println("Operation timed out after 10 seconds: " + e.getMessage());
            // Optionally cancel the task if it times out
            futureResult.cancel(true);
        }

        // CompletableFuture with chaining
        CompletableFuture<Integer> completableFuture = asyncMethodWithCompletableFuture(5)
                .thenApply(n -> n + 1)
                .thenApply(n -> n * 2);

        Integer finalResult = completableFuture.get();
        System.out.println("CompletableFuture result: " + finalResult);

        // Demonstrate alternative async implementations
        CompletableFuture<Integer> executorResult = asyncMethodWithExecutor(10)
                .thenApply(n -> n + 5);
        System.out.println("Executor result: " + executorResult.get());

        CompletableFuture<Integer> forkJoinResult = asyncMethodWithForkJoinPool(15)
                .thenApply(n -> n * 3);
        System.out.println("ForkJoinPool result: " + forkJoinResult.get());

        CompletableFuture<Integer> customResult = asyncMethodWithCustomExecutor(20)
                .thenApply(n -> n / 2);
        System.out.println("Custom executor result: " + customResult.get());
    }
}
