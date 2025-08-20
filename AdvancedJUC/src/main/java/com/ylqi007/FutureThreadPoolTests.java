package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;


/**
 *
 *
 */
public class FutureThreadPoolTests {

    // Use multi thread
    // 线程复用，尽量不要 new ==> 线程池
    @Test
    public void main() throws ExecutionException, InterruptedException, TimeoutException {
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

        System.out.println(futureTask1.get());  // get()，不见不散，非要等到结果才会离开，会阻塞程序
        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get(500, TimeUnit.MICROSECONDS));

        long endTime = System.currentTimeMillis();

        threadPool.shutdown();

        System.out.println("Total cost time = " + (endTime - startTime) + "ms");    // Total cost time = 606ms
        System.out.println(Thread.currentThread().getName() + " ends");
    }

    @Test
    public void main1() {
        // 3 tasks, but only one main() to process. How long it will take
        // 3 个任务，但是只有一个 main 线程，需要多少时间才能处理完？
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

        System.out.println("Total cost time = " + (endTime - startTime) + "ms");    // Total cost time = 1511ms
        System.out.println(Thread.currentThread().getName() + " ends");
    }

    // isDone()轮询---轮询的方式会耗费无谓的cpu资源
    @Test
    public void main2() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 1 is done";
        });
        Thread thread = new Thread(futureTask1);
        thread.start();

        System.out.println(Thread.currentThread().getName() + " 在忙其他任务");

        while (true) {
            if(futureTask1.isDone()) {
                System.out.println(futureTask1.get());
                break;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("正在处理中，不要再催了！");
            }
        }
//
//        threadPool.shutdown();
//
//        System.out.println("Total cost time = " + (endTime - startTime) + "ms");    // Total cost time = 606ms
        System.out.println(Thread.currentThread().getName() + " ends");
    }
}
