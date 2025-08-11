package com.ylqi007.chap04locksupport;

import com.ylqi007.utils.CommonUtils;

/**
 * 1. wait() and notify() 必须要在同步块或者方法里面，且成对出现使用
 * 2. 先 wait(), then notify()
 */
public class Demo01SynchronizedWaitNotify {
    public static void main(String[] args) {
        // test01();
        // testException01();

        testException02();
    }

    private static void test01() {
        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
            }
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            synchronized (lock) {
                lock.notify();
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            }
        }, "t2").start();
    }


    /**
     * t1	 ---- come in
     * Exception in thread "t1" java.lang.IllegalMonitorStateException: current thread is not owner
     * 	at java.base/java.lang.Object.wait(Native Method)
     * 	at java.base/java.lang.Object.wait(Object.java:338)
     * 	at com.ylqi007.chap04locksupport.LockSupportDemo1.lambda$testException01$2(LockSupportDemo1.java:47)
     * 	at java.base/java.lang.Thread.run(Thread.java:840)
     * Exception in thread "t2" java.lang.IllegalMonitorStateException: current thread is not owner
     * 	at java.base/java.lang.Object.notify(Native Method)
     * 	at com.ylqi007.chap04locksupport.LockSupportDemo1.lambda$testException01$3(LockSupportDemo1.java:60)
     * 	at java.base/java.lang.Thread.run(Thread.java:840)
     *
     * 两个异常，wait() and notify() 必须在 synchronized () {...} 内部被调用
     */
    private static void testException01() {
        Object lock = new Object();

        new Thread(() -> {
//            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
//            }
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            //synchronized (lock) {
                lock.notify();
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            //}
        }, "t2").start();
    }


    /**
     * t2	 -- 发出唤醒通知
     * t1	 ---- come in
     * 程序卡死
     */
    private static void testException02() {
        Object lock = new Object();

        new Thread(() -> {
            CommonUtils.sleepSeconds(1);    // sleep 1s，让 t2 先获得锁，先调用 notify()
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
            }
        }, "t1").start();

        // CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            synchronized (lock) {
                lock.notify();
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            }
        }, "t2").start();
    }
}
