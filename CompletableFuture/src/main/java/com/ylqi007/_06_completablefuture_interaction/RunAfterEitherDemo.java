package com.ylqi007._06_completablefuture_interaction;

import com.ylqi007.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunAfterEitherDemo {
    public static void main(String[] args) {

        // 异步任务1
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            int x = new Random().nextInt(4);
            CommonUtils.sleepSeconds(x);
            CommonUtils.printThreadLog("Task 1: sleep " + x + " seconds");
            return "Result 1";
        });

        // 异步任务2
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            int y = new Random().nextInt(4);
            CommonUtils.sleepSeconds(y);
            CommonUtils.printThreadLog("Task 2: sleep " + y + " seconds");
            return "Result 2";
        });

        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 哪个异步任务先完成，就通知任务完成
        CompletableFuture<Void> future = future1.runAfterEitherAsync(future2, () -> {
            CommonUtils.printThreadLog("有一个异步任务已经完成");
        }, executor);

        CommonUtils.sleepSeconds(4);

        executor.shutdown();
        CommonUtils.printThreadLog("final result = " + future.join());
    }
}
