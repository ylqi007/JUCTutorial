package com.ylqi007._03_completablefuture_callback;

import com.ylqi007.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenApply(Function<T, R>)
 * 可以对异步任务的结果进一步转换，使用 Function 进行转换
 */

public class ThenApplyDemo01 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CommonUtils.printThreadLog("main() start");

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件");
            String filterWordContent = CommonUtils.readFile("filter_words.txt");
            System.out.println(filterWordContent);
            return filterWordContent;
        });

        // String --> List<String>
        CompletableFuture<String[]> filterWordsFuture = stringCompletableFuture.thenApply(content -> {
            CommonUtils.printThreadLog("把文件内容转换为 String[]");
            String[] split = content.split(",");
            System.out.println(Arrays.toString(split));
            return split;
        });

        CommonUtils.printThreadLog("main() continue");

        System.out.println("filter words = " + Arrays.toString(filterWordsFuture.get()));

        CommonUtils.printThreadLog("main() end");
    }
}
