package com.ylqi007.chap06volatile;

import com.ylqi007.utils.CommonUtils;

/**
 * Description:
 *
 */
public class Demo02VolatileSee {

    public static void main(String[] args) {
        testCannotStop();

        // testCanStop();
    }

    /**
     * t1	 ---- come in
     * main	 ---- 修改完成，flag = false
     *  陷入死循环
     */
    private static boolean flag = true;
    private static void testCannotStop() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---- come in");
            while (flag) {

            }
            System.out.println(Thread.currentThread().getName() + "\t ---- come out");
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        // 修改并没有及时刷新到主内存，通知其他线程，导致线程 t1 仍然使用的旧值，因此，无法退出循环
        flag = false;

        System.out.println(Thread.currentThread().getName() + "\t ---- 修改完成，flag = " + flag);
    }

    /**
     * t1	 ---- come in
     * t1	 ---- come out: volatileFlag = false
     * main	 ---- 修改完成，flag = false
     */
    private static volatile boolean volatileFlag = true;
    private static void testCanStop() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---- come in");
            while (volatileFlag) {

            }
            System.out.println(Thread.currentThread().getName() + "\t ---- come out: volatileFlag = " + volatileFlag);
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        // 修改并没有及时刷新到主内存，通知其他线程，导致线程 t1 仍然使用的旧值，因此，无法退出循环
        volatileFlag = false;

        System.out.println(Thread.currentThread().getName() + "\t ---- 修改完成，flag = " + volatileFlag);
    }
}
