package com.ylqi007._03_completablefuture_callback;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;


/**
 * thenRun(Runnable)
 * 当异步任务完成后，我们只想得到异步任务完成的一个通知，不实用上一步异步任务的结果，就可以使用 thenRun()
 * 通常用在链式操作的末端
 */
public class ThenRunDemo01 {
    public static void main(String[] args) {
        CommonUtils.printThreadLog("main() start");

        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件 -- 开始");
            return CommonUtils.readFile("filter_words.txt");
        }).thenRun(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件 -- 完成");
        });

        CommonUtils.printThreadLog("main() continue");
        CommonUtils.sleepSeconds(2);
        CommonUtils.printThreadLog("main() end");
    }
}
