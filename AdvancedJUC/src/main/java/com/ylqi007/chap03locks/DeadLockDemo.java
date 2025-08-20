package com.ylqi007.chap03locks;

public class DeadLockDemo {
    public static void main(String[] args) {
        final Object lockA = new Object();
        final Object lockB = new Object();

        new Thread(() -> {
            synchronized (lockA) {
                System.out.println(Thread.currentThread().getName() + "持有lockA，尝试获得lockB");
                synchronized (lockB) {
                    System.out.println(Thread.currentThread().getName() + "持有lockA，成功获得lockB");
                }
            }
        }, "Thread-1").start();

        new Thread(() -> {
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "持有lockB，尝试获得lockA");
                synchronized (lockA) {
                    System.out.println(Thread.currentThread().getName() + "持有lockB，成功获得lockA");
                }
            }
        }, "Thread-2").start();
    }
}
