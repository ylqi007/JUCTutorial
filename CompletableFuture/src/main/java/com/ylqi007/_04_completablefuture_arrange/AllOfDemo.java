package com.ylqi007._04_completablefuture_arrange;

import com.ylqi007.utils.CommonUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * allOf() 适合合并多个异步任务，当所有的异步任务都完成时，可以进一步操作
 */
public class AllOfDemo {
    public static void main(String[] args) {
        // 需求：统计news1.txt、new2.txt、new3.txt 文件中包含CompletableFuture关键字的文件的个数

        // Step 1: 创建 List 集合，存储文件名
        List<String> fileNames = List.of("news1.txt", "news2.txt", "news3.txt");

        // Step 2: 根据文件名调用 readFile 创建多个 CompletableFuture，并存入 List 集合中
        List<CompletableFuture<String>> completableFutureList = fileNames.stream()
                .map(fileName -> readFileFuture(fileName))
                .toList();

        // Step 3: allOf() 只能接收可变参数，不能直接接收集合，所以需要将 List 集合转换为数组。因为可变参数，本质就是可变数组
        // 把 List 集合转成数组，以便传入 allOf() 中
        CompletableFuture[] completableFutureArray = completableFutureList.toArray(CompletableFuture[]::new);


        // Step 4: 调用 allOf() 方法合并多个异步任务
        // allFuture 中没有存任何返回对象，但是有了这个对象，就可以执行它的回调
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(completableFutureArray);

        // Step 5: 当多个异步任务都完成后，使用回调操作文件结果，统计符合条件的文件个数
        // 调用 allOfFuture.thenApply() 时，说明所有的 CF 都已经完成了，即 completableFutureList 中的 future 都已经计算完成了，可以通过 join() 获取结果了
        // get() 需要抛出异常，非常不适合在 stream 中使用
        CompletableFuture<Long> countFuture = allOfFuture.thenApply(v -> {
            return completableFutureList.stream()
                    .map(future -> future.join())
                    .filter(content -> content.contains("CompletableFuture"))
                    .count();
        });

        // Step 6: main 线程打印输出文件个数
        System.out.println("count = " + countFuture.join());
    }

    private static CompletableFuture<String> readFileFuture(String fileName) {
        return CompletableFuture.supplyAsync(() -> {
            CommonUtils.printThreadLog("读取文件" + fileName);
            return CommonUtils.readFile(fileName);
        });
    }
}
