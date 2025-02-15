package com.ylqi007._09_compare_price;

import java.util.Arrays;
import java.util.List;

/**
 * 方案二：Future + 线程池，提高了任务处理的并行性：cost time: 2.032s （4线程）
 *       Future + 线程池，提高了任务处理的并行性：cost time: 4.04s （2线程）
 *
 *  方案三测试：使用CompletableFuture进一步增强并行: cost time: 1.018s
 */
public class ComparePriceDemo {
    public static void main(String[] args) {
        ComparePriceService service = new ComparePriceService();

        // 方案一测试：串行方式操作商品比价
//        long start = System.currentTimeMillis();
//        PriceResult priceResult = service.getCheapestPlatformPrice("iPhone14");
//        long end = System.currentTimeMillis();
//        System.out.println("cost time: " + (end - start) / 1000.0 + "s");
//        System.out.println("priceResult: " + priceResult);

        // 方案二测试：使用Future+线程池增强并行
//        long start = System.currentTimeMillis();
//        PriceResult priceResult = service.getCheapestPlatformPrice2("iPhone14");
//        long end = System.currentTimeMillis();
//        System.out.println("cost time: " + (end - start) / 1000.0 + "s");
//        System.out.println("priceResult = " + priceResult);

        // 方案三测试：使用CompletableFuture进一步增强并行
//        long start = System.currentTimeMillis();
//        PriceResult priceResult = service.getCheapestPlatformPrice3("iPhone");
//        long end = System.currentTimeMillis();
//        System.out.println("cost time: " + (end - start) / 1000.0 + "s");
//        System.out.println("priceResult = " + priceResult);

        // 异步任务的批量操作
        // 测试在一个平台比较同款产品(iPhone14)不同色系的价格
        List<String> products = Arrays.asList("iPhone14黑色", "iPhone14白色", "iPhone14玫瑰红");
        PriceResult priceResult = service.batchComparePrice(products);
        System.out.println("priceResult = " + priceResult);
    }
}


/*
方案三测试：使用CompletableFuture进一步增强并行
13:56:39.462 | 18 | ForkJoinPool.commonPool-worker-4 | 获取京东上iPhone优惠
13:56:39.462 | 17 | ForkJoinPool.commonPool-worker-3 | 获取京东上iPhone价格
13:56:39.462 | 19 | ForkJoinPool.commonPool-worker-5 | 获取拼多多上iPhone价格
13:56:39.462 | 16 | ForkJoinPool.commonPool-worker-2 | 获取淘宝上iPhone优惠
13:56:39.462 | 15 | ForkJoinPool.commonPool-worker-1 | 获得淘宝上iPhone价格
13:56:39.462 | 20 | ForkJoinPool.commonPool-worker-6 | 获取拼多多上iPhone优惠
13:56:40.466 | 20 | ForkJoinPool.commonPool-worker-6 | 获取拼多多上iPhone优惠完成:-5300
13:56:40.466 | 18 | ForkJoinPool.commonPool-worker-4 | 获取京东上iPhone优惠完成:-150
13:56:40.466 | 17 | ForkJoinPool.commonPool-worker-3 | 获取京东上iPhone价格完成:5299
13:56:40.466 | 16 | ForkJoinPool.commonPool-worker-2 | 获取淘宝上iPhone优惠完成:-200
13:56:40.466 | 19 | ForkJoinPool.commonPool-worker-5 | 获取拼多多上iPhone价格完成:5300
13:56:40.466 | 15 | ForkJoinPool.commonPool-worker-1 | 获得淘宝上iPhone价格完成：5199
13:56:40.467 | 15 | ForkJoinPool.commonPool-worker-1 | 淘宝最终价格计算完成:4999
13:56:40.467 | 19 | ForkJoinPool.commonPool-worker-5 | 拼多多最终价格计算完成:99
13:56:40.467 | 18 | ForkJoinPool.commonPool-worker-4 | 京东最终价格计算完成:5149
cost time: 1.018s
priceResult = PriceResult(price=5399, discount=5300, realPrice=99, platform=拼多多)

 */