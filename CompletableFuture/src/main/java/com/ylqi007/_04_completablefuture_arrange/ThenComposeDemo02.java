package com.ylqi007._04_completablefuture_arrange;

import com.ylqi007.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
 *  重点在于对上一步异步任务中的结果 T 进行应用转换，经过 Function 回调转换后的结果 U 是一个简单的值
 *
 * public <U> CompletableFuture<U> thenCompose(Function<? super T,? extends CompletionStage<U>> fn)
 *   重点在于对上一步异步任务中的结果 T 进行应用转换，经过 Function 回调转换后的结果是一个 CompletableFuture 对象
 *      T 是上一步异步任务的结果
 *
 * 结论：thenCompose() 用来连接两个有依赖关系的异步任务，结果由第二个任务返回
 */
public class ThenComposeDemo02 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 编排两个依赖关系的任务: thenCompose()

        CommonUtils.printThreadLog("main() start");

        CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件");
            return CommonUtils.readFile("filter_words.txt");
        }).thenCompose(content -> CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("文件内容转换为 String[]");
            return content.split(",");
        }));

        CommonUtils.printThreadLog("main() continue");

        System.out.println("filter words = " + Arrays.toString(filterWordsFuture.get()));

        CommonUtils.printThreadLog("main() end");
    }

}
