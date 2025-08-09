package com.ylqi007.chap02completablefuture;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 对计算结果进行处理
 *  * thenApply ---> 计算结果存在依赖关系，这两个线程串行化 ----> 由于存在依赖关系（当前步错，不走下一步），当前步骤有异常的话就叫停
 *      CompletableFuture1 --> CompletableFuture2 --> ...
 *  * handle ---> 计算结果存在依赖关系，这两个线程串行化 ----> 有异常也可以往下走一步
 */
@Log4j2
public class CompletableFutureAPI2ThenApplyHandleTest {
    private static final int THREAD_COMPLETION_WAIT_SECONDS = 3;

    /**
     * 此 test 使用了 ExecutorService，要在 main 中测试
     * 此时使用的就是自定义线程池，即 pool-1
     */
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("111");
            return 1;
        }, threadPool).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            System.out.println("222");
            // 模拟异常
            int i = 10 / 0;
            return f + 2;
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            System.out.println("333");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + v);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        threadPool.shutdown();  // 否在会一直等待
    }


    /**
     * supplyAsync() --> thenApply() --> thenApply() --> whenComplete()
     */
    @Test
    public void testThenApplyWhenComplete() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            threadSleep(1);
            System.out.println("111 is done.");
            return 1;
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            System.out.println("222 is done.");
            return f + 2;
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            System.out.println("333 is done.");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + v);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();
            System.out.println(e.getMessage());

            log.error("Thread was interrupted while waiting for completion", e);
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    /**
     * supplyAsync() --> thenApply() --> thenApply() --> whenComplete() --> exceptionally()
     */
    @Test
    public void testThenApplyWhenCompleteException() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            threadSleep(1);
            System.out.println("111 is done.");
            return 1;
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            // 模拟异常
            int i = 10 / 0;     // 当前步骤有异常，运行叫停. 不会执行后续的 thenApply，也不会执行 whenComplete，直接到 exceptionally
            System.out.println("222 is done.");
            return f + 2;
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            System.out.println("333 is done.");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + v);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    /**
     * Test handle, 有异常也可以继续往下执行
     * handle(BiFunction)
     *
     * supplyAsync(Supplier) --> handle(Function) --> handle(Function) --> whenComplete(BiConsumer)
     */
    @Test
    public void testHandleWhenComplete() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            threadSleep(1);
            System.out.println("111 is done.");
            return 1;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            System.out.println("222 is done.");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            System.out.println("333 is done.");
            return f + 3;
        }).whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + r);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();

            log.error("Thread was interrupted while waiting for completion", e);
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    /**
     * Test handle, 有异常也可以继续往下执行
     * supplyAsync() --> handle(BiFunction) --> handle(BiFunction) --> whenComplete() --> exceptionally()
     */
    @Test
    public void testHandleWhenCompleteException() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            threadSleep(1);
            System.out.println("111 is done.");
            return 1;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            int i = 10 / 0; // 模拟异常
            System.out.println("222 is done.");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            int i = 10 / 0; // 模拟异常
            System.out.println("333 is done.");
            return f + 3;
        }).whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + r);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();

            log.error("Thread was interrupted while waiting for completion", e);
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
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
            System.out.println(Thread.currentThread().getName() + " is working on :: return 1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("111");
            return 1;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 2");
            // int i = 10 / 0;
            System.out.println("222");
            return f + 2;
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: return f + 3");
            System.out.println("333");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: whenComplete()");
            if(e == null) {
                System.out.println("计算结果 = " + v);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: exceptionally()");
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

    private void threadSleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            // 如果你想确认当前运行时到底用的哪个日志实现，可以在代码里加：
            System.out.println(org.apache.logging.log4j.LogManager.getContext().getClass().getName());
            Thread.currentThread().interrupt(); // restore interrupt status
            log.error("Thread was interrupted while waiting for completion", e);
        }
    }


    /**
     * Test thenApply without thread pool
     * 使用的是默认线程池：ForkJoinPool.commonPool
     */
    @Test
    public void testThenApply() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Step 1: 买鱼");
            threadSleep(1);
            return "Step 1: 买完鱼了";
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working Step 2");
            // int i = 10 / 0;  // 当前步骤有异常，运行叫停. 不会执行后续的 thenApply，也不会执行 whenComplete，直接到 exceptionally
            System.out.println("Step 2: 做鱼");
            return f + " -> " + "Step 2: 做完鱼了";
        }).thenApply(f -> {
            System.out.println(Thread.currentThread().getName() + " is working Step 3");
            // int i = 10 / 0;  // 当前步骤有异常，运行叫停. 不会执行后续的 thenApply，也不会执行 whenComplete，直接到 exceptionally
            System.out.println("Step 3: 吃鱼");
            return f + " -> " + "Step 3: 吃完鱼了";
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾：\n" + v);
            }
        }).exceptionally(e -> {
            System.out.println(Thread.currentThread().getName() + " is working exceptionally()");
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    /**
     * Backup
     */
    /**
     * Test handle, 有异常也可以继续往下执行
     * handle(BiFunction)
     */
    @Test
    public void testHandleWhenCompleteBackup() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 1");
            System.out.println("Step 1: 买鱼");
            threadSleep(1);
            return "Step 1: 买完鱼了";
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 2");
            System.out.println("Step 2: 做鱼");
            return f + " -> " + "Step 2: 做完鱼了"; // Step 1: 买完鱼了 -> Step 2: 做完鱼了
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 3");
            System.out.println("Step 3: 吃鱼");
            return f + " -> " + "Step 3: 吃完鱼了"; // Step 1: 买完鱼了 -> Step 2: 做完鱼了 -> Step 3: 吃完鱼了
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾: " + v);
            }
        }).exceptionally(e -> {
            log.error("{} is working exceptionally().", Thread.currentThread().getName(), e);
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    /**
     * Test handle, 有异常也可以继续往下执行
     */
    @Test
    public void testHandleExceptionBackup() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 1");
            threadSleep(1);
            System.out.println("Step 1: 买鱼");
            return "Step 1: 买完鱼了";
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 2");
            int i = 10 / 0;     // 这步有异常，无法处理result1，并返回结果。但是 Step3 还可以继续
            System.out.println("Step 2: 做鱼");    // 不会运行到这一行
            return f + " -> " + "Step 2: 做完鱼了"; // 不会运行到这一行
        }).handle((f, e) -> {
            // int i = 10 / 0;  // 这步有异常，无法处理result1，并返回结果。直接到 exceptionally
            System.out.println("Result from the last step = " + f); // Result from the last step = null
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 3");
            System.out.println("Step 3: 吃鱼");
            return f + " -> " + "Step 3: 吃完鱼了"; // null -> Step 3: 吃完鱼了
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾：\n" + v);
            }
        }).exceptionally(e -> {
            log.error("{} is working exceptionally().", Thread.currentThread().getName(), e);
            System.out.println(Thread.currentThread().getName() + " is working exceptionally().");
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }

    @Test
    public void testHandleException2Backup() {
        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 1");
            threadSleep(1);
            System.out.println("Step 1: 买鱼");
            return "Step 1: 买完鱼了";
        }).handle((f, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 2");
            System.out.println("Step 2: 做鱼");
            return f + " -> " + "Step 2: 做完鱼了";
        }).handle((f, e) -> {
            System.out.println("Result from the last step = " + f); // f = "Step 1: 买完鱼了 -> Step 2: 做完鱼了"
            System.out.println(Thread.currentThread().getName() + " is working on :: Step 3");
            int i = 10 / 0;  // 这步有异常，无法处理result2，并返回结果。直接到 exceptionally
            System.out.println("Step 3: 吃鱼");
            return f + " -> " + "Step 3: 吃完鱼了"; // null -> Step 3: 吃完鱼了
        }).whenComplete((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " is working whenComplete()");
            if(e == null) {
                System.out.println("吃饭结束，步骤回顾：\n" + v);
            }
        }).exceptionally(e -> {
            log.error("{} is working exceptionally().", Thread.currentThread().getName(), e);
            System.out.println(Thread.currentThread().getName() + " is working exceptionally().");
            return null;
        });

        System.out.println(Thread.currentThread().getName() + " completed other tasks");

        threadSleep(THREAD_COMPLETION_WAIT_SECONDS);
    }
}
