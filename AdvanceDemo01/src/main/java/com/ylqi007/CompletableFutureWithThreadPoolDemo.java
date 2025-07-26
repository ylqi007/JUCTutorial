package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureWithThreadPoolDemo {

    @Test
    public void test1() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on ForkJoinPool.commonPool-worker-1
                return "Result of task 1";
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on ForkJoinPool.commonPool-worker-1
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    @Test
    public void test2() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on ForkJoinPool.commonPool-worker-1
                return "Result of task 1";
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on ForkJoinPool.commonPool-worker-2
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    @Test
    public void test3() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on pool-1-thread-1
                return "Result of task 1";
            }, threadPool).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on pool-1-thread-1
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on pool-1-thread-1
            });
            voidCompletableFuture.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    @Test
    public void test4() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on pool-1-thread-1
                return "Result of task 1";
            }, threadPool).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on ForkJoinPool.commonPool-worker-1
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }
}
