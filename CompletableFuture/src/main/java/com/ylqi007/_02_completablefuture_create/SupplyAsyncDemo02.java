package com.ylqi007._02_completablefuture_create;


import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SupplyAsyncDemo02 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：开启异步任务读取 news.txt 文件中的新闻内容，返回文件中的内容，并在主线程打印输出
        CommonUtils.printThreadLog("main() start");

        // Lambda 表达式
        CompletableFuture<String> newsCompletableFuture = CompletableFuture.supplyAsync(() -> CommonUtils.readFile("news.txt"));

        CommonUtils.printThreadLog("main() continue, no block");

        System.out.println(newsCompletableFuture.get());    // block

        CommonUtils.printThreadLog("main() end");
    }
}
