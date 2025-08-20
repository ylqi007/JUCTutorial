package com.ylqi007.chap02completablefuture;

import com.ylqi007.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * CompletableFuture 常用方法1: 获取结果和触发计算
 *  * get()                 一直等待，造成阻塞
 *  * get(long, TimeUnit)   过时不侯
 *  * join()    和get一样的作用，只是不需要抛出异常
 *  * public T getNow(T valuelfAbsent) --->计算完成就返回正常值，否则返回备胎值（传入的参数），立即获取结果不阻塞
 *
 * 触发计算
 *  * public boolean complete(T value) ---->是否打断get方法立即返回括号值
 */
public class CompletableFutureAPI1GetJoinCompleteTest {

    /**
     * public T get()
     *       throws InterruptedException, ExecutionException
     * Waits if necessary for this future to complete, and then returns its result.
     *
     * CompletableFuture.get()  一直等待直到拿到结果，会造成阻塞。
     */
    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            CommonUtils.sleepSeconds(2);
            return "Task is done! With throws Exceptions in method signature";
        });

        System.out.println(completableFuture.get());    // CompletableFuture.get() 会造成线程 block
    }

    /**
     * public T get(long timeout, TimeUnit unit)
     *       throws InterruptedException, ExecutionException, TimeoutException
     * Waits if necessary for at most the given time for this future to complete, and then returns its result, if available.
     *
     * CompletableFuture.get(timeout, TimeUnit) 过时不侯，规定时间内完成，返回结果，否会抛出 Timeout 异常
     */
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    public void testGetTimeout(long timeout) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            CommonUtils.sleepSeconds(2);
            return "Task is done!";
        });

        try {
            String result = completableFuture.get(timeout, TimeUnit.SECONDS);
            System.out.printf("Timeout %d seconds: %s%n", timeout, result);
        } catch (TimeoutException e) {
            System.out.printf("Timeout %d seconds: java.util.concurrent.TimeoutException(过时不候) %n", timeout);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        /*
        ForkJoinPool.commonPool-worker-1 is running
        Timeout 1 seconds: java.util.concurrent.TimeoutException(过时不候)
        ForkJoinPool.commonPool-worker-2 is running
        Timeout 2 seconds: java.util.concurrent.TimeoutException(过时不候)
        ForkJoinPool.commonPool-worker-2 is running
        Timeout 3 seconds: Task is done!
         */
    }

    /**
     * public T join()
     * Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally.
     *
     * 作用和 get() 类似，只是不需要抛出异常
     */
    @Test
    public void testJoin() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            CommonUtils.sleepSeconds(2);
            return "Task is done! Without throwing exceptions";
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
            CommonUtils.sleepSeconds(2);
            return "Task is done!";
        });

        CommonUtils.sleepSeconds(1);
        System.out.println(completableFuture.getNow("返回默认值: 任务没有完成"));
    }


    /**
     * public boolean complete(T value)
     * If not already completed, sets the value returned by get() and related methods to the given value.
     *  是否打断 get() 方法立刻返回括号值。
     *
     * 作用与 getNow() 类似，getNow() 返回默认结果，complete() 返回 boolean 值，表示是否打断
     *
     * Sleep 1s, then return: true	complete value, 即主动打断 stringCompletableFuture，返回 "complete value"
     * Sleep 3s, then return: false	abs, 也就是 stringCompletableFuture 没有被打断，返回的就是 CompletableFuture 计算的结果
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 3})
    public void testComplete(int seconds) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            CommonUtils.sleepSeconds(2);
            return "Task is done!";
        });

        CommonUtils.sleepSeconds(seconds);

        boolean wasCompleted = future.complete("打断Task，任务没有完成");
        System.out.printf(
                "main thread sleep %ds → complete() returned: %b, result: %s%n",
                seconds, wasCompleted, future.join()
        );
    }
}
