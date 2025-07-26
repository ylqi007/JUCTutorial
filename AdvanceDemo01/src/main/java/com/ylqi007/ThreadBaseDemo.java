package com.ylqi007;

public class ThreadBaseDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {}, "Thread-01");
        t1.start();

        Object obj = new Object();
        new Thread(() -> {
            synchronized (obj) {

            }
        }, "Thread-02").start();
    }
}
