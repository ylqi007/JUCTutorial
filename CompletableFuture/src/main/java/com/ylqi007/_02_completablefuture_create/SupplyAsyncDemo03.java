package com.ylqi007._02_completablefuture_create;


import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupplyAsyncDemo03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 需求：**指定线程池**，开启异步任务读取 news.txt 文件中的新闻内容，返回文件中的内容，并在主线程打印输出
        CommonUtils.printThreadLog("main() start");

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // Lambda 表达式
        CompletableFuture<String> newsCompletableFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("开始异步读取文件。。。");
            return CommonUtils.readFile("news.txt");
        }, executorService);

        CommonUtils.printThreadLog("main() continue, no block");

        System.out.println(newsCompletableFuture.get());    // block

        // 关闭线程池
        executorService.shutdown();
        CommonUtils.printThreadLog("main() end");
    }
}
