package com.atguigu;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class CompletableFutureBuildDemo {

    /**
     * Use default thread pool
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCompletableFuture01() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // ForkJoinPool.commonPool-worker-1
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(completableFuture.get());
    }

    /**
     * Use specific thread pool defined by user
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testCompletableFuture02() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool);

        System.out.println(voidCompletableFuture.get());

        threadPool.shutdown();
    }

    @Test
    public void testCompletableFuture03() throws ExecutionException, InterruptedException {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // ForkJoinPool.commonPool-worker-1
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Task is done";
        });

        System.out.println(stringCompletableFuture.get());
    }

    @Test
    public void testCompletableFuture04() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // pool-1-thread-1
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "CompletableFuture.supplyAsync() is done";
        }, threadPool);

        System.out.println(stringCompletableFuture.get());
        threadPool.shutdown();
    }
}
