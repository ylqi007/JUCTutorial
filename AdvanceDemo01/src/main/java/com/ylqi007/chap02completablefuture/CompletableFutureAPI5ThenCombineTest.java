package com.ylqi007.chap02completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI5ThenCombineTest {

    @Test
    public void testThenCombine() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start");
            threadSleep(1);
            return 10;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start");
            threadSleep(2);
            return 20;
        });

        // 以上是两个异步任务: future1 and future2
        CompletableFuture<Integer> result = future1.thenCombine(future2, (x, y) -> {
            System.out.println(Thread.currentThread().getName() + " 开始两个结果的合并");
            System.out.println("Result of future1: x = " + x);
            System.out.println("Result of future2: y = " + y);
            return x * y;
        });

        System.out.println(result.join());
    }


    @Test
    public void testThenCombine2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start at 1");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start at 2");
            return 20;
        }), (x, y) -> {
            System.out.println("x = " + x + ", y = " + y);
            System.out.println(Thread.currentThread().getName() + " start at 3");
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start at 4");
            return 30;
        }), (a, b) -> {
            System.out.println("a = " + a + ", b = " + b);
            System.out.println(Thread.currentThread().getName() + " start at 5");
            return a + b;
        });

        System.out.println("main thread ends");
        System.out.println(thenCombineResult.get());
    }

    private void threadSleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
