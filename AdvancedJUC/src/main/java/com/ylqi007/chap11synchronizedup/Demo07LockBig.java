package com.ylqi007.chap11synchronizedup;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  9:16 AM
 * @Description:
 */
public class Demo07LockBig {
    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
//            synchronized (lock) {
//                System.out.println("1");
//            }
//            synchronized (lock) {
//                System.out.println("22");
//            }
//            synchronized (lock) {
//                System.out.println("333");
//            }
//            synchronized (lock) {
//                System.out.println("4444");
//            }

            // ==> 锁粗大
            synchronized (lock) {
                System.out.println("1");
                System.out.println("22");
                System.out.println("333");
                System.out.println("4444");
            }
        }, "t1").start();
    }
}
