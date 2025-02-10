package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 对处理结果进行消费：Consumer
 *  * thenRun(Runnable runnable) :任务A执行完执行B，并且不需要A的结果
 *  * thenAccept(Consumer action): 任务A执行完执行B，B需要A的结果，但是任务B没有返回值
 *  * thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值
 *
 * 对比补充: 任务之间的顺行执行
 *  thenRun(Runnable) : () -> { take some action }
 *  thenAccept(Consumer) : (a) -> { consume a, without return }
 *  thenApply(Funtion fn) : (T) -> { return U }
 */
public class CompletableFutureAPI3Demo {

    @Test
    public void test00() {
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenRun(() -> {}).join());    // null
//        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenAccept(r -> System.out.println(r)).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenAccept(System.out::println).join());  // ReulstA, null
        System.out.println(CompletableFuture.supplyAsync(() -> "ResultA").thenApply(r -> r + " ResultB").join());
    }


    // 任务A执行完执行B，B需要A的结果，同时任务B有返回值
    @Test
    public void test01() {
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(f -> f + 2)
                .thenApply(f -> f + 3)
                .thenAccept(System.out::println);   // 消费计算结果，无返回结果

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");
    }

    // thenRun(Runnable) : () -> { take some action }，无返回结果，所以打印时输出 null
    // 任务A执行完执行B，并且不需要A的结果
    @Test
    public void test02() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Task A: Result A")
                        .thenRun(() -> System.out.println("Task B: " + Thread.currentThread().getName()))
                        .join() // thenRun是 Runnable，没有返回值，所以输出 null
        );
    }

    // thenAccept(Consumer) : (a) -> { consume a, without return }
    @Test
    public void test03() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Task A: Result A")
                        .thenAccept(r -> System.out.println(Thread.currentThread().getName() + ": ---> " + r))
                        .join() // 因为是消费型，thenAccept并没有返回值，所以输出null
        );
    }

    // thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值
    @Test
    public void test04() {
        System.out.println(
                CompletableFuture.supplyAsync(() -> "Task A: Result A") // 启动一个有返回值的任务线程
                        .thenApply(r -> Thread.currentThread().getName() + ": ---> " + r + " ---> " + "Result B")
                        .join() // thenApply 是有返回值的
        );
    }
}
