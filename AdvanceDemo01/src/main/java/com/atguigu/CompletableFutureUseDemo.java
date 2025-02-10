package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class CompletableFutureUseDemo {

    /**
     * CompletableFuture 可以取代 Future 的功能
     */
    @Test
    public void test01() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");   // ForkJoinPool.commonPool-worker-1 is running
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Get result after 2s: " + result);
            return result;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");    // main is working on other tasks
        System.out.println(integerCompletableFuture.get()); // get() 会造成阻塞
    }

    /**
     * main线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭。做法：暂停3s
     * --> 因此，日常中尽量使用自己的线程池
     */
    @Test
    public void test02() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");   // ForkJoinPool.commonPool-worker-1 is running
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Get result after 2s: " + result);
            return result;
        }).whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println(Thread.currentThread().getName() + "计算完成，更新value=" + result);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("计算时发生异常：" + e.getCause() + "\t" + e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");    // main is working on other tasks
        // main线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭。做法：暂停3s
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日常中尽量使用自己的线程池
     */
//    @Test
//    public void test03() {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " is running");   // ForkJoinPool.commonPool-worker-1 is running
                int result = ThreadLocalRandom.current().nextInt(10);
                System.out.println("Get result before 2s: " + result);   // 有结果
                if(result > 4) {
                    int i = result / 0;
                }
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Get result after 2s: " + result);
                return result;
            }, threadPool).whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println(Thread.currentThread().getName() + "计算完成，更新value=" + result);
                }
                System.out.println(Thread.currentThread().getName() + " 进入 whenComplete() 方法");
            }) .exceptionally(e -> {
                e.printStackTrace();
                System.out.println("计算时发生异常：" + e.getCause() + "\t" + e.getMessage());
                return null;
            });
            System.out.println(Thread.currentThread().getName() + " is working on other tasks");    // main is working on other tasks
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

        // System.out.println(Thread.currentThread().getName() + " is working on other tasks");    // main is working on other tasks
    }

}
