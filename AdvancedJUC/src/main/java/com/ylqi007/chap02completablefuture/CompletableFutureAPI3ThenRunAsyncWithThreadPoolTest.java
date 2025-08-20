package com.ylqi007.chap02completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI3ThenRunAsyncWithThreadPoolTest {

    /**
     * 调用 thenRun(Runnable)
     * 自定义了线程池，但是没有使用。因此，使用默认的线程池：ForkJoinPool.commonPool
     */
    @Test
    public void testThenRunWithoutThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                threadSleep(1);
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on ForkJoinPool.commonPool-worker-1
                return "Result of task 1";
            }).thenRun(() -> {
                threadSleep(1);
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on ForkJoinPool.commonPool-worker-1
            }).thenRun(() -> {
                threadSleep(1);
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(4L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }


    /**
     * 调用 thenRunAsync(Runnable)
     * 自定义了线程池，但是没有使用。
     * 虽然调用的是 thenRunAsync(), 但是，依然使用默认的线程池：ForkJoinPool.commonPool
     */
    @Test
    public void testThenRunAsyncWithoutThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                threadSleep(1);
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on ForkJoinPool.commonPool-worker-1
                return "Result of task 1";
            }).thenRunAsync(() -> {
                threadSleep(1);
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on ForkJoinPool.commonPool-worker-2
            }).thenRunAsync(() -> {
                threadSleep(1);
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(4L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * CompletableFuture.supplyAsync(Supplier, threadPool) + thenRun(Runnable)
     *
     * CompletableFuture.supplyAsync() 使用了自定义的线程池，后续的 thenRun()，都继续使用了相同的线程, pool-1-thread-1
     */
    @Test
    public void testThenRunWithThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                threadSleep(1);
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on pool-1-thread-1
                return "Result of task 1";
            }, threadPool).thenRun(() -> {
                threadSleep(1);
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on pool-1-thread-1
            }).thenRun(() -> {
                threadSleep(1);
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on pool-1-thread-1
            });
            voidCompletableFuture.get(4L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * CompletableFuture.supplyAsync(Supplier, threadPool) + thenRunAsync(Runnable)
     * thenRunAsync() 另起炉灶，使用其他的线程池
     */
    @Test
    public void test4() {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                threadSleep(1);
                System.out.println("Task 1 is running on " + Thread.currentThread().getName()); // Task 1 is running on pool-1-thread-1
                return "Result of task 1";
            }, threadPool).thenRun(() -> {
                threadSleep(1);
                System.out.println("Task 2 is running on " + Thread.currentThread().getName()); // Task 2 is running on pool-1-thread-1
            }).thenRunAsync(() -> {
                threadSleep(1);
                System.out.println("Task 3 is running on " + Thread.currentThread().getName()); // Task 3 is running on ForkJoinPool.commonPool-worker-1
            });
            voidCompletableFuture.get(4L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    private void threadSleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
