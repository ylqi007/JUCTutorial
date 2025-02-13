package com.ylqi007._03_completablefuture_callback;

import com.ylqi007.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * thenAccept(Consumer<T> action)
 * 对异步任务的返回结果进行消费。
 * 通常作为回调链中的最后一个回调。
 */
public class ThenAcceptDemo01 {
    public static void main(String[] args) {
        CommonUtils.printThreadLog("main() start");

        CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件");
            return CommonUtils.readFile("filter_words.txt");
        }).thenApply(content -> {
            CommonUtils.printThreadLog("将敏感词内容转换为 String[]");
            return content.split(",");
        }).thenAccept(filterWords -> {
            System.out.println(Arrays.toString(filterWords));
        });

        CommonUtils.printThreadLog("main() continue");
        CommonUtils.sleepSeconds(2);
        CommonUtils.printThreadLog("main() end");
    }
}
