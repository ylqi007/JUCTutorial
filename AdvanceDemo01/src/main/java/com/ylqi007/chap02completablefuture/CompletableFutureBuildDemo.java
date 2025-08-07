package com.ylqi007.chap02completablefuture;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * CompletableFuture.runAsync(Runnable) 无返回值
 * CompletableFuture.supplyAsync(Supplier) 有返回值
 */
public class CompletableFutureBuildDemo {

    /**
     * Use default thread pool: ForkJoinPool
     * CompletableFuture.runAsync(Runnable runnable)
     *  无参数
     *  无返回值
     */
    @Test
    public void testCompletableFuture01() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // ForkJoinPool.commonPool-worker-1, 默认线程池
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(completableFuture.get());    // null
    }

    /**
     * Use specific thread pool defined by user: pool-1-thread-1
     */
    @Test
    public void testCompletableFuture02() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // pool-1-thread-1，使用用户自定义线程池
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool);

        System.out.println(voidCompletableFuture.get());    // null

        threadPool.shutdown();
    }

    /**
     * CompletableFuture.supplyAsync()
     *  无参数
     *  有返回值
     */
    @Test
    public void testCompletableFuture03() throws ExecutionException, InterruptedException {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());   // ForkJoinPool.commonPool-worker-1
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Task is done";  // 有返回值
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
