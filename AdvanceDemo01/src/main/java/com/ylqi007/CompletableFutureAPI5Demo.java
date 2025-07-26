package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI5Demo {
    @Test
    public void test1() {
        CompletableFuture<Integer> integerCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        });

        CompletableFuture<Integer> integerCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " start");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 20;
        });

        // 以上是两个异步任务
        CompletableFuture<Integer> result = integerCompletableFuture1.thenCombine(integerCompletableFuture2, (x, y) -> {
            System.out.println(Thread.currentThread().getName() + " 开始两个结果的合并");
            return x * y;
        });

        System.out.println(result.join());
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
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
}
