package com.ylqi007._09_compare_price;

import com.ylqi007.utils.CommonUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

public class ComparePriceService {
    // 方案一：串行方式操作商品比价
    public PriceResult getCheapestPlatformPrice(String productName) {
        PriceResult priceResult;
        int discount;

        // 获取淘宝平台的商品价格和优惠
        priceResult = HttpRequest.getTaoBaoPrice(productName);
        discount = HttpRequest.getTaoBaoDiscount(productName);
        PriceResult taobalPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取京东平台的商品价格和优惠
        priceResult = HttpRequest.getJDongPrice(productName);
        discount = HttpRequest.getJDongDiscount(productName);
        PriceResult jDongPriceResult = this.computeRealPrice(priceResult, discount);

        // 获取拼多多平台的商品价格和优惠
        priceResult = HttpRequest.getPDDPrice(productName);
        discount = HttpRequest.getPDDDiscount(productName);
        PriceResult pddPriceResult = this.computeRealPrice(priceResult, discount);

        // 计算最优的平台和价格
        return Stream.of(taobalPriceResult, jDongPriceResult, pddPriceResult)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .get();
    }

    // 方案二: 使用Future+线程池增强并行
    public PriceResult getCheapestPlatformPrice2(String productName) {
        // 线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 获取淘宝平台的商品价格和优惠
        Future<PriceResult> taoBaoFuture = executor.submit(() -> {
            PriceResult taoBaoPrince = HttpRequest.getTaoBaoPrice(productName);
            int taoBaoDiscount = HttpRequest.getTaoBaoDiscount(productName);
            return this.computeRealPrice(taoBaoPrince, taoBaoDiscount);
        });

        // 获取京东平台的商品价格和优惠
        Future<PriceResult> jDongFuture = executor.submit(() -> {
            PriceResult priceResult = HttpRequest.getJDongPrice(productName);
            int discount = HttpRequest.getJDongDiscount(productName);
            return this.computeRealPrice(priceResult, discount);
        });

        // 获取拼多多平台的商品价格和优惠
        Future<PriceResult> pddFuture = executor.submit(() -> {
            PriceResult priceResult = HttpRequest.getPDDPrice(productName);
            int discount = HttpRequest.getPDDDiscount(productName);
            return this.computeRealPrice(priceResult, discount);
        });

        // 计算最优的平台和价格
        return Stream.of(taoBaoFuture, jDongFuture, pddFuture)
                .map(future -> {
                    try {
                        return future.get(5, TimeUnit.SECONDS);    // 此处Future 不是 CompletableFuture, 没有 join() 方法
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        executor.shutdown();
                    }
                })
                .filter(Objects::nonNull)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .orElse(new PriceResult());
    }

    // 方案三: 使用CompletableFuture进一步增强并行
    public PriceResult getCheapestPlatformPrice3(String productName) {

        // 获取淘宝平台的商品价格和优惠
        CompletableFuture<PriceResult> taoBaoCF = CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoDiscount(productName)),
                        (priceResult, discount) -> this.computeRealPrice(priceResult, discount));

        // 获取京东平台的商品价格和优惠
        CompletableFuture<PriceResult> jDongCF = CompletableFuture.supplyAsync(() -> HttpRequest.getJDongPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getJDongDiscount(productName)), this::computeRealPrice);

        // 获取拼多多平台的商品价格和优惠
        CompletableFuture<PriceResult> pddCF = CompletableFuture.supplyAsync(() -> HttpRequest.getPDDPrice(productName))
                .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getPDDDiscount(productName)), this::computeRealPrice);


        // 比价计算最便宜的平台和价格信息
        return Stream.of(taoBaoCF, jDongCF, pddCF)
                .map(CompletableFuture::join)
                .min(Comparator.comparing(PriceResult::getRealPrice))
                .orElse(new PriceResult());
    }

    public PriceResult batchComparePrice(List<String> products) {
        // step 1: 遍历每个商品的名字，根据商品名称开启异步任务获取最终价，归集到List集合中
        List<CompletableFuture<PriceResult>> completableFutures = products.stream()
                .map(productName -> {
                    return CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoPrice(productName))
                            .thenCombine(CompletableFuture.supplyAsync(() -> HttpRequest.getTaoBaoDiscount(productName)), (priceResult, discount) -> this.computeRealPrice(priceResult, discount));
                }).toList();

        // step 2: 把多个商品的最终价进行排序获取最小值
        return completableFutures
                .stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparing(PriceResult::getRealPrice))
                .findFirst()
                .get();
    }


    // 计算商品的最终价格 = 平台价格 - 优惠价
    public PriceResult computeRealPrice(PriceResult priceResult, int discount) {
        priceResult.setRealPrice(priceResult.getPrice() - discount);
        priceResult.setDiscount(discount);
        CommonUtils.printThreadLog(priceResult.getPlatform() + "最终价格计算完成:" + priceResult.getRealPrice());
        return priceResult;
    }
}
