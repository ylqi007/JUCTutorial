package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 线程池之间的
 */
public class CompletableFutureAPI3Demo2 {

    /**
     * thenRun
     */
    @Test
    public void testDefaultThreadPool1() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1: " + Thread.currentThread().getName());
                return "Task 1: completed";
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2: " + Thread.currentThread().getName());
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3: " + Thread.currentThread().getName());
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 4: " + Thread.currentThread().getName());
            });

            System.out.println(voidCompletableFuture.get(2L, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * 结果与 thenRun 一致
     */
    @Test
    public void testDefaultThreadPool2() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1: " + Thread.currentThread().getName());
                return "Task 1: completed";
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2: " + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3: " + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 4: " + Thread.currentThread().getName());
            });

            System.out.println(voidCompletableFuture.get(2L, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Task 1 指定线程池，task2，3，4会和 task 1 实用同一个线程池
     */
    @Test
    public void testCustomizedThreadPool1() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1: " + Thread.currentThread().getName());
                return "Task 1: completed";
            }, threadPool).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2: " + Thread.currentThread().getName());
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3: " + Thread.currentThread().getName());
            }).thenRun(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 4: " + Thread.currentThread().getName());
            });

            System.out.println(voidCompletableFuture.get(2L, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Task 1: pool-1-thread-1
     * Task 2: ForkJoinPool.commonPool-worker-1
     * Task 3: ForkJoinPool.commonPool-worker-2
     * Task 4: ForkJoinPool.commonPool-worker-2
     */
    @Test
    public void testCustomizedThreadPool2() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 1: " + Thread.currentThread().getName());
                return "Task 1: completed";
            }, threadPool).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 2: " + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 3: " + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task 4: " + Thread.currentThread().getName());
            });

            System.out.println(voidCompletableFuture.get(2L, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

}
