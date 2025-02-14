package com.ylqi007._08_completablefuture_parallelstream;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 观察发现：使用 CompletableFuture 和使用并行流的时间大致一样
 *
 * 能够进一步优化？
 * CompletableFutures 比 ParallelStream 优点之一是你可以指定Executor去处理任务。你能选择更合适数量的线程。
 * 我们可以选择大于 Runtime.getRuntime().availableProcessors() 数量的线程
 */
public class CompletableFutureDemo02 {
    public static void main(String[] args) {
        // CompletableFuture 在流式操作的优势
        // 需求：创建10MyTask耗时的任务，统计它们执行完的总耗时

        // 方案三：使用CompletableFuture
        // step 1: 创建10个MyTask对象，每个任务持续1s，存入List集合
        IntStream intStream = IntStream.range(0, 30);
        List<MyTask> myTasks = intStream.mapToObj(num -> new MyTask(1)).toList();

        // 准备线程池
        int N_CPU = Runtime.getRuntime().availableProcessors();
        System.out.println("N_CPU = " + N_CPU);

        // 设置线程池的数量最少是10个
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(30, 2 * N_CPU));

        // step 2: 根据MyTask对象构建10个耗时的异步任务
        List<CompletableFuture<Integer>> futures = myTasks.stream()
                .map(myTask -> CompletableFuture.supplyAsync(() -> myTask.doWork(), executor))
                .toList();

        // step 3: 当所有任务完成时，获取每个异步任务的执行结果，存入List集合中
        long startTime = System.currentTimeMillis();

        List<Integer> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        long endTime = System.currentTimeMillis();

        // 30 tasks: 完成30个任务总耗时 = 2s
        // 100 tasks: 完成100个任务总耗时 = 5s
        System.out.println("完成10个任务总耗时 = " + (endTime - startTime)/1000 + "s"); // 完成10个任务总耗时 = 1s

        executor.shutdown();
    }
}

/*
N_CPU = 12
1739518761126 | 22 | pool-1-thread-8 | doWork()...
1739518761126 | 21 | pool-1-thread-7 | doWork()...
1739518761126 | 19 | pool-1-thread-5 | doWork()...
1739518761126 | 20 | pool-1-thread-6 | doWork()...
1739518761126 | 15 | pool-1-thread-1 | doWork()...
1739518761126 | 17 | pool-1-thread-3 | doWork()...
1739518761126 | 16 | pool-1-thread-2 | doWork()...
1739518761126 | 23 | pool-1-thread-9 | doWork()...
1739518761126 | 24 | pool-1-thread-10 | doWork()...
1739518761126 | 18 | pool-1-thread-4 | doWork()...
完成10个任务总耗时 = 1s
 */