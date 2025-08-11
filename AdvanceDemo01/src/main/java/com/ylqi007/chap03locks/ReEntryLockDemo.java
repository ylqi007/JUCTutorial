package com.ylqi007.chap03locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本质上使用的是同一把锁
 * 简单来说，在一个 synchronized 修饰的方法或代码块内部调用本类的其他synchronized修饰的方法或代码块时，是永远可以得到锁的。
 */
public class ReEntryLockDemo {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "----外层调用");

                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "----中层调用");

                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "----内层调用");
                    } finally {
                        System.out.println(Thread.currentThread().getName() + "----内层调用::结束");
                        lock.unlock();  // comment this line ==> 会造成死锁
                    }
                } finally {
                    System.out.println(Thread.currentThread().getName() + "----中层调用::结束");
                    lock.unlock();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + "----外层调用::结束");
                lock.unlock();
            }
        }, "Thread-1").start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "----外层调用");

                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "----中层调用");

                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "----内层调用");
                    } finally {
                        System.out.println(Thread.currentThread().getName() + "----内层调用::结束");
                        lock.unlock();
                    }
                } finally {
                    System.out.println(Thread.currentThread().getName() + "----中层调用::结束");
                    lock.unlock();
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + "----外层调用::结束");
                lock.unlock();
            }
        }, "Thread-2").start();
    }

    // Demo 2: 隐式可重入锁，synchronized
    public static void reEntrySynchronized(String[] args) {
        ReEntryLockDemo reEntryLockDemo = new ReEntryLockDemo();

        new Thread(reEntryLockDemo::method1, "Thread-1").start();
    }

    private synchronized void method1() {
        // 指的是可重复递归调用的说，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁，这样的锁就叫做可重入锁。
        System.out.println(Thread.currentThread().getName() + " starts method1()");
        method2();
        System.out.println(Thread.currentThread().getName() + " ends method1()");
    }

    private synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + " starts method2()");
        method3();
        System.out.println(Thread.currentThread().getName() + " ends method2()");
    }

    private synchronized void method3() {
        System.out.println(Thread.currentThread().getName() + " starts method3()");
        System.out.println(Thread.currentThread().getName() + " ends method3()");
    }


    // main() --> reEntryCodeBlock
    public static void reEntryCodeBlock(String[] args) {
        final Object object = new Object();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "----外层调用");
                synchronized (object) {
                    System.out.println(Thread.currentThread().getName() + "----中层调用");
                    synchronized (object) {
                        System.out.println(Thread.currentThread().getName() + "----外内调用");
                    }
                };
            }
        }, "Thread-1").start();
    }
}
