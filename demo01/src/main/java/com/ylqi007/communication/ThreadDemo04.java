package com.ylqi007.communication;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadDemo04 {
    public static void main(String[] args) {
        Share04 share = new Share04();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.print5(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.print10(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.print15(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "CC").start();
    }
}


class Share04 {
    // Define flag
    private int flag = 1;   // AA 1; BB 2; CC 3

    // Create Lock
    private Lock lock = new ReentrantLock();

    // Define 3 conditions
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5(int loop) throws InterruptedException {
        // lock
        lock.lock();

        try {
            while (flag != 1) {
                c1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ":: loop" + loop + "::" + i);
            }

            // Notify
            flag = 2;
            c2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void print10(int loop) throws InterruptedException {
        // lock
        lock.lock();

        try {
            while (flag != 2) {
                c2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ":: loop" + loop + "::" + i);
            }

            // Notify
            flag = 3;
            c3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void print15(int loop) throws InterruptedException {
        // lock
        lock.lock();

        try {
            while (flag != 3) {
                c3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + ":: loop" + loop + "::" + i);
            }

            // Notify
            flag = 1;
            c1.signal();
        } finally {
            lock.unlock();
        }
    }
}