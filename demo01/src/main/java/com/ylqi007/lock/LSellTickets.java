package com.ylqi007.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: 使用ReentLock实现卖票
 *  Step 1. 创建资源类，定义属性和操作方法
 *  Step 2. 创建多个线程，调用资源类得操作方法
 * @Author: ylqi007
 * @Create: 11/22/24 22:56
 *
 * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html
 */

public class LSellTickets {


    public static void main(String[] args) {

        Ticket ticket = new Ticket();
        //创建三个线程
        // Runnable是函数式接口，可以用Lambda表达式的写法
        // Thread t1: AA
        new Thread(() -> {
            //调用卖票得方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "AA").start();

        // Thread t2: BB
        new Thread(() -> {
            //调用卖票得方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "BB").start();

        // Thread t3: CC
        new Thread(() -> {
            //调用卖票得方法
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "CC").start();
    }
}


// Step 1.创建资源类 定义属性和操作方法
class Ticket {
    // 创建可重入锁, ReentrantLock
    private final ReentrantLock lock = new ReentrantLock();

    // 属性: 票数
    private int number = 30;

    // 操作方法: 卖票
    public void sale() {
        // 上锁
        lock.lock();

        try {
            //判断是否可以卖票 数量-1
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + " :卖出: 1" + " 剩下: " + --number);
            }
        } finally {
            //解锁
            lock.unlock();
        }
    }
}
