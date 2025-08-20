package com.ylqi007.chap07cas;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  10:49 PM
 * @Description: 多线程环境中使用 synchronized 保证 i++ 的线程安全
 */
public class Demo01Synchronized {

    public static void main(String[] args) {

    }

    private volatile int count = 0;
    // 写操作：若要线程安全执行 count++, 需要加锁
    public synchronized void increment() {
        count++;
    }

    // 读操作：不需要加锁
    public int getCount() {
        return count;
    }
}
