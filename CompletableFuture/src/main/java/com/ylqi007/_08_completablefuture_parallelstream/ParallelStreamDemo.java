package com.ylqi007._08_completablefuture_parallelstream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamDemo {
    public static void main(String[] args) {
        // 需求：创建10个MyTask耗时的任务，统计它们执行完的总耗时
        // 方案二：使用并行流
        // step 1: 创建10个MyTask对象，每个任务持续1s，存入List集合
        IntStream intStream = IntStream.range(0, 100);   // [0, ..., 9]
        List<MyTask> tasks = intStream.mapToObj(num -> new MyTask(1))
                .toList();

        // step 2: 执行10个MyTask,统计总耗时
        long startTime = System.currentTimeMillis();

        List<Integer> results = tasks.parallelStream()
                .map(MyTask::doWork)
                .toList();

        long endTime = System.currentTimeMillis();

        // 30 tasks: 完成30个任务总耗时 = 3s
        // 100 tasks: 完成100个任务总耗时 = 9s
        System.out.println("完成10个任务总耗时 = " + (endTime - startTime)/1000 + "s"); // 完成10个任务总耗时 = 1s

    }
}

/*
1739518463115 | 21 | ForkJoinPool.commonPool-worker-7 | doWork()...
1739518463115 | 18 | ForkJoinPool.commonPool-worker-4 | doWork()...
1739518463115 | 23 | ForkJoinPool.commonPool-worker-9 | doWork()...
1739518463115 | 16 | ForkJoinPool.commonPool-worker-2 | doWork()...
1739518463115 | 22 | ForkJoinPool.commonPool-worker-8 | doWork()...
1739518463115 |  1 | main | doWork()...
1739518463115 | 17 | ForkJoinPool.commonPool-worker-3 | doWork()...
1739518463115 | 20 | ForkJoinPool.commonPool-worker-6 | doWork()...
1739518463115 | 19 | ForkJoinPool.commonPool-worker-5 | doWork()...
1739518463115 | 15 | ForkJoinPool.commonPool-worker-1 | doWork()...
完成10个任务总耗时 = 1s
 */