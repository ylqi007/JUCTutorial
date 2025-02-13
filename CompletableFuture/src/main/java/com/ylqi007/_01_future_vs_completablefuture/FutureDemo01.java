package com.ylqi007._01_future_vs_completablefuture;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 读文件 --> I/O 操作，耗时任务
 * 如何替换操作很多 --> 也会是耗时任务
 */
public class FutureDemo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Step 1: 读取敏感词汇 --> Thread1
        Future<String[]> filterWordsFuture = executor.submit(() -> {
            String str = CommonUtils.readFile("filter_words.txt");
            return str.split(",");
        });

        // Step 2: 读取新闻内容 --> Thread2
        Future<String> newsFuture = executor.submit(() -> CommonUtils.readFile("news.txt"));


        // Step 3: 替换操作 --> Thread3 : 依赖 Step 1 & 2 的结果
        Future<String> replacedNewsFuture = executor.submit(() -> {
            String[] words = filterWordsFuture.get();      // blocking
            String news = newsFuture.get();    // blocking

            for (String word : words) {
                if (news.indexOf(word) > -1) {
                    news = news.replace(word, "**");
                }
            }
            return news;
        });

        // Step 4: 打印输出替换后的新闻内容 --> main
        System.out.println(replacedNewsFuture.get());   // blocking
    }
}


/**
 * 1. 在没有阻塞的情况下，无法对Future的结果执行进一步操作。比如需要在for-loop中替换敏感词时，需要等到 words，news 都完成，在答应最后替换后的新闻内容时，需要等到 replacedNewsFuture 计算完成。不具备 Future 完成计算后，其结果自动调用 callback 的能力
 * 2. 无法解决任务组合相互依赖的问题。filterWordsFuture 和 newsFuture 不能自动发给 replacedNewsFuture。--> 不能轻易创建异步工作流。expected: filterWordsFuture + newsFuture --> replacedNewsFuture
 * 3. 不能将多个 Future 合并在一起。Future 很难独立完成这一需求。
 * 4. 没有异常处理。需要开发者手动处理。
 */