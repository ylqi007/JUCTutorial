package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPIDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("111");
            return 1;
        }, threadPool).thenApply(f -> {
            System.out.println("222");
            return f + 2;
        }).thenApply(f -> {
            System.out.println("333");
            return f + 3;
        }).whenComplete((v, e) -> {
            if(e == null) {
                System.out.println("计算结果 = " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");
    }

    /**
     * 获得结果 & 触发计算
     *  * public T get()
     *  * public T get(long timeout,TimeUnit unit)
     *  * public T join() --->和get一样的作用，只是不需要抛出异常
     *  * public T getNow(T valuelfAbsent) --->计算完成就返回正常值，否则返回备胎值（传入的参数），立即获取结果不阻塞
     *
     * 触发计算
     *  * public boolean complete(T value) ---->是否打断get方法立即返回括号值
     */
    @Test
    public void test01() {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abs";
        });

        // System.out.println(stringCompletableFuture.get());   // 需要抛出异常
        // System.out.println(stringCompletableFuture.get(2L, TimeUnit.SECONDS));
        // System.out.println(stringCompletableFuture.join());

        //try {
        //    TimeUnit.SECONDS.sleep(3);
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
        // System.out.println(stringCompletableFuture.getNow("No value returned"));

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Sleep 1s, then return: true	complete value, 即主动打断 stringCompletableFuture，返回 "complete value"
        // Sleep 3s, then return: false	abs, 也就是 stringCompletableFuture 没有被打断，返回的就是 CompletableFuture 计算的结果
        System.out.println(stringCompletableFuture.complete("complete value") + "\t" + stringCompletableFuture.join());
    }
}
