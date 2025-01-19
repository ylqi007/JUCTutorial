package com.atguigu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 对计算结果进行处理
 *  * thenApply ---> 计算结果存在依赖关系，这两个线程串行化 ----> 由于存在依赖关系（当前步错，不走下一步），当前步骤有异常的话就叫停
 *      CompletableFuture1 --> CompletableFuture2 --> ...
 *  * handle ---> 计算结果存在依赖关系，这两个线程串行化 ----> 有异常也可以往下走一步
 */
public class CompletableFutureAPI2Demo {

    /**
     * 此 test 使用了 ExecutorService，要在 main 中测试
     * @param args
     */
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

        threadPool.shutdown();  // 否在会一直等待
    }


    /**
     * Test thenApply without thread pool
     */
    @Test
    public void testThenApply() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Step 1: 买鱼");
            return "Step 1: 买完鱼了";
        }).thenApply(f -> {
            // int i = 10 / 0;  // 当前步骤有异常，运行叫停. 不会执行后续的 thenApply，也不会执行 whenComplete，直接到 exceptionally
            System.out.println("Step 2: 做鱼");
            return f + "\n" + "Step 2: 做完鱼了";
        }).thenApply(f -> {
            // int i = 10 / 0;  // 当前步骤有异常，运行叫停. 不会执行后续的 thenApply，也不会执行 whenComplete，直接到 exceptionally
            System.out.println("Step 3: 吃鱼");
            return f + "\n" + "Step 3: 吃完鱼了";
        }).whenComplete((v, e) -> {
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾：\n" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test handle, 有异常也可以继续往下执行
     */
    @Test
    public void test02Handle() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Step 1: 买鱼");
            return "Step 1: 买完鱼了";
        }).handle((f, e) -> {
            // int i = 10 / 0; // 这步有异常，无法处理result1，并返回结果。但是 Step3 还可以继续
            System.out.println("Step 2: 做鱼");
            return f + "\n" + "Step 2: 做完鱼了";
        }).handle((f, e) -> {
            int i = 10 / 0;  // 这步有异常，无法处理result1，并返回结果。直接到 exceptionally
            System.out.println("Step 3: 吃鱼");
            return f + "\n" + "Step 3: 吃完鱼了";
        }).whenComplete((v, e) -> {
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾：\n" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test03ProcessResults() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("111");
            return 1;
        }).thenApply(f -> {
            // int i = 10 / 0;
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

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test04() {
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("111");
            return 1;
        }).handle((f, e) -> {
            int i = 10 / 0;
            System.out.println("222");
            return f + 2;
        }).handle((f, e) -> {
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

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对处理结果进行消费：Consumer
     *  * thenRun(Runnable runnable) :任务A执行完执行B，并且不需要A的结果
     *  * thenAccept(Consumer action): 任务A执行完执行B，B需要A的结果，但是任务B没有返回值
     *  * thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值
     */
    // 任务A执行完执行B，B需要A的结果，同时任务B有返回值
    @Test
    public void test05() {
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(f -> f + 2)
                .thenApply(f -> f + 3)
                .thenAccept(System.out::println);

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");
    }

    // 任务A执行完执行B，并且不需要A的结果
    @Test
    public void test06() {
        System.out.println(CompletableFuture.supplyAsync(() -> "Result A")
                .thenRun(() -> System.out.println(Thread.currentThread().getName()))
                .join());
    }

    @Test
    public void test07() {
        System.out.println(CompletableFuture.supplyAsync(() -> "Result A")
                .thenAccept(r -> System.out.println(Thread.currentThread().getName() + ": ---> " + r))
                .join());
    }

    // thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值
    @Test
    public void test08() {
        System.out.println(CompletableFuture.supplyAsync(() -> "Result A")
                .thenApply(r -> Thread.currentThread().getName() + ": ---> " + r + " ---> " + "Result B")
                .join());
    }
}
