package com.ylqi007._06_completablefuture_interaction;

import com.ylqi007.utils.CommonUtils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * 异步任务交互指，哪个结果先到，就先用哪个结果。
 */
public class AppyToEitherDemo {
    public static void main(String[] args) {

        // 异步任务1
        CompletableFuture<Integer> futureX = CompletableFuture.supplyAsync(() -> {
            int x = new Random().nextInt(4);
            CommonUtils.sleepSeconds(x);
            CommonUtils.printThreadLog("Task 1: sleep " + x + " seconds");
            return x;
        });

        // 异步任务2
        CompletableFuture<Integer> futureY = CompletableFuture.supplyAsync(() -> {
            int y = new Random().nextInt(4);
            CommonUtils.sleepSeconds(y);
            CommonUtils.printThreadLog("Task 2: sleep " + y + " seconds");
            return y;
        });

        // 哪个异步任务先完成，就先用哪个异步任务的结果
        CompletableFuture<Integer> future = futureX.applyToEither(futureY, result -> {
            CommonUtils.printThreadLog("最先到到的结果: " + result);
            return result;
        });

        CommonUtils.sleepSeconds(4);

        CommonUtils.printThreadLog("final result = " + future.join());
    }
}
