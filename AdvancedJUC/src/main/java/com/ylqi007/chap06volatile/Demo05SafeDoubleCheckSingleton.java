package com.ylqi007.chap06volatile;

/**
 * @Author: ylqi007
 * @Date: 8/13/25  10:35 PM
 * @Description:
 */
public class Demo05SafeDoubleCheckSingleton {
    // 通过 volatile 声明，实现线程安全的初始化
    private static volatile Demo05SafeDoubleCheckSingleton singleton;

    // 私有化构造器
    private Demo05SafeDoubleCheckSingleton() {}

    // 双重锁设计
    public static Demo05SafeDoubleCheckSingleton getInstance() {
        if (singleton == null) {
            // 1. 多线程创建对象时，会通过加锁保证只有一个线程能创建对象
            synchronized (Demo05SafeDoubleCheckSingleton.class) {
                if (singleton == null) {
                    // 隐患：多线程环境下，由于重排序，该对象可能还未完成初始化就被其他线程读取
                    // 解决：使用 volatile，禁止"初始化对象(step 2)"和"设置singleton指向内存空间(step 3)"的重排序
                    singleton = new Demo05SafeDoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }

}
