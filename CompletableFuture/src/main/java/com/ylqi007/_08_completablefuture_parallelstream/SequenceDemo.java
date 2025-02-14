package com.ylqi007._08_completablefuture_parallelstream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SequenceDemo {
    public static void main(String[] args) {
        // 需求：创建10个MyTask耗时的任务，统计它们执行完的总耗时
        // 方案一：在主线程中使用串行执行
        // step 1: 创建10个MyTask对象,每个任务持续1s,存入list集合便于启动Stream操作
        IntStream intStream = IntStream.range(0, 10);   // [0, ..., 9]
        List<MyTask> tasks = intStream.mapToObj(num -> new MyTask(1))
                .collect(Collectors.toList());

        // step 2: 执行tasks集合中的每个任务，统计总耗时
        long startTime = System.currentTimeMillis();

        List<Integer> results = tasks.stream()
                .map(myTask -> {
                    return myTask.doWork();
                }).toList();

        long endTime = System.currentTimeMillis();

        System.out.println("完成10个任务总耗时 = " + (endTime - startTime) / 1000 + "s");   // 完成10个任务总耗时 = 10s
    }
}
