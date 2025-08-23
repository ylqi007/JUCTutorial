package com.ylqi007.chap09threadlocal;

import com.ylqi007.utils.CommonUtils;

import java.util.Random;

/**
 * @Author: ylqi007
 * @Date: 8/21/25  10:17 PM
 * @Description:
 */
public class Demo01SellHouses {
    public static void main(String[] args) {
        // test01();

        test02();
    }

    /**
     * 需求1: 5个销售卖房子，集团高层只关注销售总量的准确统计数。
     */
    private static void test01() {
        House house = new House();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                int size = new Random().nextInt(5) + 1;
                System.out.println(Thread.currentThread().getName() + "\t" + "卖出 " + size + " 套");;
                for (int j = 0; j < size; j++) {
                    house.saleHouse();
                }
            }, "t" + i).start();
        }

        CommonUtils.sleepSeconds(1);

        System.out.println(Thread.currentThread().getName() + "\t" + "总共卖出 = " + house.saleCount);
    }

    /**
     * 需求2: 5个销售卖完房子，各自独立销售额度，自己业绩按提成走，分灶吃饭，各个销售自己动手，丰衣足食
     */
    private static void test02() {
        House house = new House();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                int size = new Random().nextInt(5) + 1;
                try {
                    for (int j = 0; j < size; j++) {
                        house.saleHouse();
                        house.saleVolumeByThreadLocal();    // ThreadLocal，即每个销售自己的
                    }
                    System.out.println(Thread.currentThread().getName() + "\t" + "卖出 " + house.saleVolumeThreadLocal.get() + " 套");;
                } finally {
                    house.saleVolumeThreadLocal.remove();   // 阿里巴巴规范：必须回收自定义的ThreadLocal变量
                }
            }, "t" + i).start();
        }

        CommonUtils.sleepSeconds(1);

        System.out.println(Thread.currentThread().getName() + "\t" + "总共卖出 = " + house.saleCount);
    }

}


// 资源类
class House {
    int saleCount = 0;

    // 大锅饭. synchronized 说明每次只有一个线程能进入、做++操作。可以精确统计，但是效率低下
    public synchronized void saleHouse() {
        saleCount++;
    }

    // 匿名内部类，实现繁琐，不推荐
//    ThreadLocal<Integer> saleVolume = new ThreadLocal<>() {
//        @Override protected Integer initialValue() {
//            return 0;
//        }
//    };

    // 分灶吃饭
    ThreadLocal<Integer> saleVolumeThreadLocal = ThreadLocal.withInitial(() -> 0);
    public void saleVolumeByThreadLocal() {
        saleVolumeThreadLocal.set(saleVolumeThreadLocal.get() + 1);
    }
}
