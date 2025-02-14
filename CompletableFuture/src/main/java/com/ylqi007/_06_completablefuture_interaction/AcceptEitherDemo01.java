package com.ylqi007._06_completablefuture_interaction;

import com.ylqi007.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class AcceptEitherDemo01 {
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

        // 哪个异步任务先完成，就先用哪个异步任务的结果: result 就是先到的结果
        CompletableFuture<Void> future = future1.acceptEither(future2, result -> {
            CommonUtils.printThreadLog("最先到到的结果: " + result);
        });

        CommonUtils.sleepSeconds(4);

        CommonUtils.printThreadLog("final result = " + future.join());
    }
}
