package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * CompletableFuture 常用方法1: 获取结果和触发计算
 *  * get()                 一直等待，造成阻塞
 *  * get(long, TimeUnit)   过时不侯
 *  * join()
 */
public class CompletableFutureAPI1Demo {

    /**
     * public T get()
     *       throws InterruptedException, ExecutionException
     * Waits if necessary for this future to complete, and then returns its result.
     *
     * CompletableFuture.get()  一直等待直到拿到结果，会造成阻塞。
     */
    @Test
    public void testGet1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is done!";
        });

        System.out.println(completableFuture.get());
    }

    /**
     * public T get(long timeout, TimeUnit unit)
     *       throws InterruptedException, ExecutionException, TimeoutException
     * Waits if necessary for at most the given time for this future to complete, and then returns its result, if available.
     *
     * CompletableFuture.get(timeout, TimeUnit) 过时不侯，规定时间内完成，返回结果，否会抛出 Timeout 异常
     */
    @Test
    public void testGet2() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is done!";
        });

        // System.out.println(completableFuture.get(2L, TimeUnit.SECONDS));    // java.util.concurrent.TimeoutException
        // System.out.println(completableFuture.get(3L, TimeUnit.SECONDS));    // java.util.concurrent.TimeoutException
        System.out.println(completableFuture.get(4L, TimeUnit.SECONDS));    // Task is done!
    }

    /**
     * public T join()
     * Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally.
     *
     * 作用和 get() 类似
     */
    @Test
    public void testJoin() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is done!";
        });

        System.out.println(completableFuture.join());
    }

    /**
     * public T getNow(T valueIfAbsent)
     * Returns the result value (or throws any encountered exception) if completed, else returns the given valueIfAbsent.
     *
     * 立即获取结果，不用阻塞。
     *  * 计算完，返回计算完成的结果
     *  * 没算完，返回设定的默认值
     */
    @Test
    public void testGetNow() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is done!";
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(completableFuture.getNow("任务没有完成"));
    }

    /**
     * public boolean complete(T value)
     * If not already completed, sets the value returned by get() and related methods to the given value.
     *  是否打断 get() 方法立刻返回括号值。
     *
     * 作用与 getNow() 类似，getNow() 返回默认结果，complete() 返回 boolean 值，表示是否打断
     */
    @Test
    public void testComplete() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is done!";
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(completableFuture.complete("打断Task，任务没有完成") + "\t" + completableFuture.join());
    }




}
