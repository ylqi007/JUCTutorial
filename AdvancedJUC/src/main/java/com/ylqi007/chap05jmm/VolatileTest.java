package com.ylqi007.chap05jmm;


public class VolatileTest {
    private volatile int value = 0;

    /**
     * 使用：把 value 定义为 volatile 变量，由于 setter 方法对 value 的修改不依赖 value 的原值，满足 volatile 关键字使用场景。
     * 理由：利用 volatile 保证读取操作的可见性；利用 synchronized 保证符合操作的原子性结合使用锁和 volatile 变量减少同时的开销。
     * @return
     */
    public int getValue() {
        return value;   // 利用 volatile 保证读取操作的可见性
    }

    public synchronized int setValue() {
        return ++value; // 利用 synchronized 保证符合操作的原子性
    }
}
