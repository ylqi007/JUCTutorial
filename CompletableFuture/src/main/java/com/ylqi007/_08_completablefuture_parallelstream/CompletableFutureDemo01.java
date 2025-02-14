package com.ylqi007._08_completablefuture_parallelstream;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * 观察发现：使用 CompletableFuture 和使用并行流的时间大致一样
 *
 * 能够进一步优化？
 * CompletableFutures 比 ParallelStream 优点之一是你可以指定Executor去处理任务。你能选择更合适数量的线程。
 * 我们可以选择大于 Runtime.getRuntime().availableProcessors() 数量的线程
 */
public class CompletableFutureDemo01 {
    public static void main(String[] args) {
        // CompletableFuture 在流式操作的优势
        // 需求：创建10MyTask耗时的任务，统计它们执行完的总耗时

        // 方案三：使用CompletableFuture
        // step 1: 创建10个MyTask对象，每个任务持续1s，存入List集合
        IntStream intStream = IntStream.range(0, 10);
        List<MyTask> myTasks = intStream.mapToObj(num -> new MyTask(1)).toList();

        // step 2: 根据MyTask对象构建10个耗时的异步任务
        List<CompletableFuture<Integer>> futures = myTasks.stream()
                .map(myTask -> CompletableFuture.supplyAsync(() -> myTask.doWork()))
                .toList();

        // step 3: 当所有任务完成时，获取每个异步任务的执行结果，存入List集合中
        long startTime = System.currentTimeMillis();

        List<Integer> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        long endTime = System.currentTimeMillis();

        System.out.println("完成10个任务总耗时 = " + (endTime - startTime)/1000 + "s"); // 完成10个任务总耗时 = 1s
    }
}

/*
1739518434716 | 16 | ForkJoinPool.commonPool-worker-2 | doWork()...
1739518434717 | 20 | ForkJoinPool.commonPool-worker-6 | doWork()...
1739518434716 | 15 | ForkJoinPool.commonPool-worker-1 | doWork()...
1739518434716 | 19 | ForkJoinPool.commonPool-worker-5 | doWork()...
1739518434717 | 21 | ForkJoinPool.commonPool-worker-7 | doWork()...
1739518434716 | 18 | ForkJoinPool.commonPool-worker-4 | doWork()...
1739518434717 | 23 | ForkJoinPool.commonPool-worker-9 | doWork()...
1739518434716 | 17 | ForkJoinPool.commonPool-worker-3 | doWork()...
1739518434717 | 22 | ForkJoinPool.commonPool-worker-8 | doWork()...
1739518434717 | 24 | ForkJoinPool.commonPool-worker-10 | doWork()...
完成10个任务总耗时 = 1s
 */