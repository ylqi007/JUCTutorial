package com.ylqi007.chap03locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SellTicketDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                ticket.sell();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                ticket.sell();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                ticket.sell();
            }
        }, "C").start();
    }
}


class Ticket {
    private int number = 30;
    private Lock lock = new ReentrantLock();

    public void sell() {
        lock.lock();
        try {
            if(number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t 卖出第: "+(number--)+"\t 还剩下: "+number);
            }
        } finally {
            lock.unlock();
        }
    }
}
