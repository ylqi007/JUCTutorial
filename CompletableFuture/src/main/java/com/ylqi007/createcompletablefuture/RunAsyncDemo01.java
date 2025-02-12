package com.ylqi007.createcompletablefuture;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class RunAsyncDemo01 {

    public static void main(String[] args) {
        CommonUtils.printThreadLog("main() start");

        // runAsync 创建异步任务。使用 Runnable 匿名内部类
        CompletableFuture.runAsync(new Runnable() {
                                       @Override
                                       public void run() {
                                           CommonUtils.printThreadLog("runAsync() start");
                                           CommonUtils.sleepSeconds(1);
                                           CommonUtils.printThreadLog("runAsync() end");
                                       }
                                   }
        );

        CommonUtils.printThreadLog("Here is not blocked. main() continue");
        CommonUtils.sleepSeconds(2);
        CommonUtils.printThreadLog("main() end");
    }

    private static void createThread() {
        // 回顾线程的创建和启动
        new Thread(() -> {
            System.out.println(CommonUtils.readFile("news.txt"));
            CommonUtils.sleepSeconds(1);
            System.out.println(CommonUtils.readFile("filter_words.txt"));
        }).start();
    }

    // 开启一个异步任务读取文件
    // CompletableFuture 中的异步任务底层通过开启线程的方式完成
    private static void asyncWayReadFile() {
        CompletableFuture.runAsync(() -> {
            System.out.println(CommonUtils.readFile("news.txt"));
            CommonUtils.sleepSeconds(1);
            System.out.println(CommonUtils.readFile("filter_words.txt"));
        });
    }
}
