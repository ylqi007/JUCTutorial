package com.ylqi007.mall;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class CompletableFutureMallDemo {

    /**
     * 使用 2 * random double + (Double)'M' 作为最终 price
     */
    @Test
    public void test01() {
        System.out.println(ThreadLocalRandom.current().nextDouble() * 2);   // 1.1272062927932116
        System.out.println(ThreadLocalRandom.current().nextDouble() * 2 + "MySQL".charAt(0));   // 77.23760800329585
    }

    /**
     * 定义 a list of NetMall
     */
    private static List<NetMall> netMallList = List.of(
            new NetMall("JD"),
            new NetMall("PDD"),
            new NetMall("TB1"),
            new NetMall("TB2"),
            new NetMall("TB3")
    );

    private static List<String> getPriceStepByStep(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        String.format(productName + " in %s price is %.2f",
                                netMall.getNetMallName(),
                                netMall.calculatePrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     * 一个线程，顺序执行
     */
    @Test
    public void testGetPricesStepByStep() {
        long startTime = System.currentTimeMillis();
        List<String> list1 = getPriceStepByStep(netMallList, "MySQL");
        list1.stream().forEach(System.out::println);
        long endTime = System.currentTimeMillis();
        System.out.println("Total cost time : " + (endTime - startTime) + " milliseconds");   // Total cost time : 5029 milliseconds
    }


    /**
     * List<NetMall> --> List<CompletableFuture<String>> --> List<String>
     * @param list
     * @param productName
     * @return List<String>
     */
    private static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        CompletableFuture.supplyAsync(() ->
                                String.format(productName + " in %s price is %.2f",
                                        netMall.getNetMallName(),
                                        netMall.calculatePrice(productName))
                        ))
                .collect(Collectors.toList())   // 'collect(toList())' can be replaced with 'toList()'
                .stream()
                .map(s -> s.join()) // or Lambda like map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Test
    public void testGetPricesByCompletableFuture() {
        long startTime = System.currentTimeMillis();
        List<String> priceList = getPriceByCompletableFuture(netMallList, "MySQL");
        priceList.stream().forEach(System.out::println);    // .stream() can be ignored
        long endTime = System.currentTimeMillis();
        System.out.println("Total cost time : " + (endTime - startTime) + " milliseconds"); // Total cost time : 1011 milliseconds
    }

}


@AllArgsConstructor
@NoArgsConstructor
@Data
class NetMall {
    private String netMallName; // JD, PDD, TB...，电商网站名字

    public double calculatePrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);  // 计算一本书的价格需要 1s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
