package com.atguigu.sync;

/**
 * Description:
 *  1. 创建资源类，在资源类中定义属性和操作方法
 *  2. 创建多个线程，调用资源类得操作方法
 *
 * @Author: ylqi007
 * @Create: 11/22/24 22:56
 */
public class SellTickets {
    //创建多个线程，调用资源类得操作方法
    public static void main(String[] args) {
        //创建Ticket对象
        Ticket ticket = new Ticket();

        //创建三个线程
        // Thread t1
        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票得方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "AA").start();

        // Thread t2
        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票得方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "BB").start();

        // Thread t3
        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票得方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "CC").start();
    }
}


//1.创建资源类 定义属性和操作方法
class Ticket {
    // 属性: 票数
    private int number = 30;

    // 操作方法: 卖票
    public synchronized void sale() {
        //判断是否可以卖票 数量-1
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + " :卖出: 1" + " 剩下: " + --number);
        }
    }
}
