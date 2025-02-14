package com.ylqi007._06_completablefuture_interaction;

import com.ylqi007.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * acceptEither: 对先到的结果进行消费
 */
public class AcceptEitherDemo02 {
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
        // 哪个异步任务先完成，就先用哪个异步任务的结果: result 就是先到的结果
        CompletableFuture<Void> future = future1.acceptEitherAsync(future2, result -> {
            CommonUtils.printThreadLog("最先到到的结果: " + result);
        }, executor);

        CommonUtils.sleepSeconds(4);

        executor.shutdown();
        CommonUtils.printThreadLog("final result = " + future.join());
    }
}
