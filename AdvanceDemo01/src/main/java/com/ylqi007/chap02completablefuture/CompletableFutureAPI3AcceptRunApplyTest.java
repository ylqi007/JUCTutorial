package com.ylqi007.chap02completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 对处理结果进行消费：Consumer
 *  * thenAccept(Consumer action) : 任务A执行完执行B，B需要A的结果，但是任务B没有返回值
 *  * thenRun(Runnable runnable) : 任务A执行完执行B，并且B不需要A的结果，相当于新起了线程任务
 *  * thenApply(Function fn) : 任务A执行完执行B，B需要A的结果，同时任务B有返回值
 *
 * 对比补充: 任务之间的顺行执行
 *  thenRun(Runnable) : () -> { take some action }
 *  thenAccept(Consumer) : (a) -> { consume a, without return }
 *  thenApply(Funtion fn) : (T) -> { return U }
 */
public class CompletableFutureAPI3AcceptRunApplyTest {

    @Test
    public void test00() {
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenRun(() -> {}).join());    // null
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenAccept(r -> System.out.println(r)).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenAccept(System.out::println).join());  // ReulstA, null
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenApply(r -> r + " ResultB").join());
    }


    /**
     * thenAccept(Consumer action): 任务A执行完执行B，B需要A的结果，但是任务B没有返回值
     * thenAccept(Consumer) : (a) -> { consume a, without return }
     */
    @Test
    public void testThenAccept() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Task A")
                        .thenAccept(r -> System.out.println("Consume 上一步结果::" + r + " :: Task B 依赖 Task A"))
                        .join() // 因为是消费型，thenAccept并没有返回值，所以输出null
        );
    }


    /**
     * thenRun(Runnable) : () -> { take some action }，无返回结果，所以打印时输出 null
     * 任务A执行完执行B，并且B不需要A的结果
     */
    @Test
    public void testThenRun() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Start Task A")
                        .thenRun(() -> System.out.println("Start Task B on thread::" + Thread.currentThread().getName()))
                        .join() // thenRun(Runnable) 没有返回值，所以输出 null
        );
    }


    /**
     * thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值
     * thenApply(Funtion fn) : (T) -> { return U }
     */
    @Test
    public void testThenApply() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Task A") // 启动一个有返回值的任务线程
                        .thenApply(r -> r + " --> " + "Task B")
                        .join() // thenApply 是有返回值的: Task A --> Task B
        );
    }


    /**
     * thenApply(Function fn): 既有输入，也有输出
     * thenAccept(Consumer): 有输入，无输出
     */
    @Test
    public void testThenApplyThenAccept() {
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(f -> f + 2)
                .thenApply(f -> f + 3)
                .thenAccept(f ->
                    System.out.println(Thread.currentThread().getName() + " is running thenAccept(Consumer), and printing result:: " + f)
                );   // 消费计算结果，无返回结果
    }





}
