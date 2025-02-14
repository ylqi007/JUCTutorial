package com.ylqi007._04_completablefuture_arrange;


import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class AnyOfDemo {
    public static void main(String[] args) {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("休眠 1 秒钟");
            CommonUtils.sleepSeconds(1);
            return "Future1 的结果";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("休眠 2 秒钟");
            CommonUtils.sleepSeconds(2);
            return "Future2 的结果";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("休眠 3 秒钟");
            CommonUtils.sleepSeconds(3);
            return "Future3 的结果";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3);

        CommonUtils.printThreadLog("res = " + anyOfFuture.join());
    }

}
