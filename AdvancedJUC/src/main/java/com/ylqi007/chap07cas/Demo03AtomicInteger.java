package com.ylqi007.chap07cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  9:14 PM
 * @Description:
 */
public class Demo03AtomicInteger {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5, 2025) + "\t" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 2025) + "\t" + atomicInteger.get());
    }
}
