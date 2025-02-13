package com.ylqi007._03_completablefuture_callback;

import com.ylqi007.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenApplyAsync(Function<T, R>)： 可以对异步任务的结果进一步转换，使用 Function 进行转换
 *  thenApply() 中回调任务和 supplyAsync() 中的异步任务使用的是同一个线程
 *      特殊：当在 supplyAsync() 中的任务是立即返回结果（不是耗时的任务，比如直接返回 return "尼玛, NB, tmd, TMD";），不需要复杂的计算，那么会直接使用 main 线程，后续的 thenApply() 回调任务也会在 main 线程上执行。
 *
 *  thenApplyAsync() : 异步回调
 *      
 */

public class ThenApplyAsyncDemo02 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CommonUtils.printThreadLog("main() start");

        // 1. 异步读取文件，得到 String
        // 2. 异步转换：String --> String[]
        CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件");
            return CommonUtils.readFile("filter_words.txt");
            // return "尼玛, NB, tmd, TMD";
        }).thenApplyAsync(content -> {
            CommonUtils.printThreadLog("把文件内容转换为 String[]");
            return content.split(",");
        });

        CommonUtils.printThreadLog("main() continue");

        System.out.println("filter words = " + Arrays.toString(filterWordsFuture.get()));

        CommonUtils.printThreadLog("main() end");
    }
}
