package com.ylqi007.chap07cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  10:53 PM
 * @Description: 多线程环境中使用原子类保证 i++ 的线程安全
 */
public class Demo02AtomicInteger {
    public static void main(String[] args) {

    }

    private AtomicInteger atomicInteger = new AtomicInteger();

    // 使用 AtomicInteger 之后，不需要加锁，也可以实现线程安全。
    public void increment() {
        atomicInteger.incrementAndGet();
    }

    public int get() {
        return atomicInteger.get();
    }
}
