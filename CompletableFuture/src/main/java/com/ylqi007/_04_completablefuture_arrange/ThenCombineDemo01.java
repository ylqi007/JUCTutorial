package com.ylqi007._04_completablefuture_arrange;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * thenCombine() 用于合并两个没有依赖关系的 Future
 *  thenCombine() 也存在异步回调的版本
 */
public class ThenCombineDemo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CommonUtils.printThreadLog("main() start");

        // 需求：
        // step 1: 读取 news.txt 中的内容
        CompletableFuture<String> newsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 news.txt 文件");
            return CommonUtils.readFile("news.txt");
        });

        // step 2: 读取 filter_words.txt 中的内容，并转换敏感词数组
        CompletableFuture<String[]> filterWordsFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取 filter_words.txt 文件");
            return CommonUtils.readFile("filter_words.txt");
        }).thenApplyAsync(content -> {
                    CommonUtils.printThreadLog("文件内容转换为 String[]");
                    return content.split(",");
        });

        // step 3: 进行替换
        CompletableFuture<String> filteredNews = newsFuture.thenCombine(filterWordsFuture, (newsContent, filterWords) -> {
            for (String word : filterWords) {
                if (newsContent.indexOf(word) > -1) {
                    newsContent = newsContent.replace(word, "**");
                }
            }
            return newsContent;
        });

        CommonUtils.printThreadLog("main() continue");

        System.out.println(filteredNews.get());

        CommonUtils.printThreadLog("main() end");
    }
}
