package com.atguigu;

import java.util.concurrent.*;

public class FutureThreadPoolDemo {

    // Use multi thread
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 3 tasks, but only one main() to process. How long it will take
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        long startTime  = System.currentTimeMillis();

        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 1 is done";
        });
        threadPool.submit(futureTask1);

        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 2 is done";
        });
        threadPool.submit(futureTask2);

        FutureTask<String> futureTask3 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 3 is done";
        });
        threadPool.submit(futureTask3);

        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get());

        long endTime = System.currentTimeMillis();

        threadPool.shutdown();

        System.out.println("Total cost time = " + (endTime - startTime) + "ms");
        System.out.println(Thread.currentThread().getName() + " ends");
    }

    public static void main1(String[] args) {
        // 3 tasks, but only one main() to process. How long it will take
        long startTime  = System.currentTimeMillis();

        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Total cost time = " + (endTime - startTime) + "ms");
        System.out.println(Thread.currentThread().getName() + " ends");
    }
}
