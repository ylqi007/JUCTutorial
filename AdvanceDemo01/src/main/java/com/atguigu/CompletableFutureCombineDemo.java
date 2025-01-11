package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ○ 两个CompletableStage任务都完成后，最终能把两个任务的结果一起交给thenCombine来处理
 * ○ 先完成的先等着，等待其他分支任务
 */
public class CompletableFutureCombineDemo {
    @Test
    public void test01() {
        CompletableFuture<Integer> integerCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is started");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        });

        CompletableFuture<Integer> integerCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is started");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 20;
        });

        System.out.println(integerCompletableFuture1.thenCombine(integerCompletableFuture2, (a, b) -> {
            System.out.println("Combining two results");
            return a + b;
        }).join());
    }

    @Test
    public void test02() {
        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is coming : 1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is coming : 2");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 20;
        }), (x, y) -> {
            System.out.println(Thread.currentThread().getName() + " is coming : 3");
            System.out.println("Combining two results = " + x + " and " + y);
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is coming : 4");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 30;
        }), (a, b) -> {
            System.out.println(Thread.currentThread().getName() + " is coming : 5");
            System.out.println("Combining two results = " + a + " and " + b);
            return a + b;
        });

        System.out.println("main thread end");
        System.out.println(thenCombineResult.join());
    }
}
