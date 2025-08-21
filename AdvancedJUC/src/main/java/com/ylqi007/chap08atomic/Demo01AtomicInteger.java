package com.ylqi007.chap08atomic;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ylqi007
 * @Date: 8/20/25  6:10 PM
 * @Description:
 * 使用 AtomicInteger 实现线程安全的 i++ 操作。
 */
public class Demo01AtomicInteger {
    private static final int SIZE = 50;

    public static void main(String[] args) throws InterruptedException {
        MyNumber myNumber = new MyNumber();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);

        System.out.println("Start time = " + System.currentTimeMillis());
        for (int i = 0; i < SIZE; i++) {
            new Thread(() -> {
                try {
                    for(int j = 0; j < 1000; j++) {
                        myNumber.addPlusPlus();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + i).start();
        }

        // 必须要等以上50个线程都完成后，再获取结果
        // CommonUtils.sleepSeconds(1);

        countDownLatch.await();
        System.out.println("End time = " + System.currentTimeMillis());

        System.out.println(Thread.currentThread().getName() + "\t Result = " + myNumber.atomicInteger.get());
    }
}


class MyNumber {
    AtomicInteger atomicInteger = new AtomicInteger(0);

    public void addPlusPlus() {
        atomicInteger.getAndIncrement();
    }
}