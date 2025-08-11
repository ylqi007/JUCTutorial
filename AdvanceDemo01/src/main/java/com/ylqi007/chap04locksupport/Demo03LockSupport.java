package com.ylqi007.chap04locksupport;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.locks.LockSupport;

/**
 * permit 许可证默认，没有则不能放行，所以一开始调 park() 方法，当前线程就会阻塞，直到别的线程给当前线程发 permit，park() 方法才会被唤醒。
 */
public class Demo03LockSupport {
    public static void main(String[] args) {

        // test01();

        // test02();

        test03();
    }

    /**
     * t1	 ---- come in
     * t1	 ---- 被唤醒
     * t2	 -- 发出唤醒通知
     */
    private static void test01() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---- come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
        }, "t1");
        t1.start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
        }, "t2").start();
    }


    /**
     * t2	 -- 发出唤醒通知
     * t1	 ---- come in
     * t1	 ---- 被唤醒
     *
     * t2 先发通知，依然可以唤醒t1
     */
    private static void test02() {
        Thread t1 = new Thread(() -> {
            CommonUtils.sleepSeconds(1);

            System.out.println(Thread.currentThread().getName() + "\t ---- come in" + "\t" + System.currentTimeMillis());
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒" + "\t" + System.currentTimeMillis());
        }, "t1");
        t1.start();

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知" + "\t" + System.currentTimeMillis());
        }, "t2").start();
    }


    /**
     *
     */
    private static void test03() {
        Thread t1 = new Thread(() -> {
            CommonUtils.sleepSeconds(1);

            System.out.println(Thread.currentThread().getName() + "\t ---- come in" + "\t" + System.currentTimeMillis());
            LockSupport.park();
            LockSupport.park(); // 只有一个permit，程序卡死
            System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒" + "\t" + System.currentTimeMillis());
        }, "t1");
        t1.start();

        new Thread(() -> {
            LockSupport.unpark(t1);
            LockSupport.unpark(t1); // permit 不会累积
            System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知" + "\t" + System.currentTimeMillis());
        }, "t2").start();
    }
}
