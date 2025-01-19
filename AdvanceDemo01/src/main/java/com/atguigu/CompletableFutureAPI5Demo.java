package com.atguigu;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;
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
}
