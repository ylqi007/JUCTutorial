package com.ylqi007.communication;

/**
 * 虚假唤醒
 */
public class ThreadDemo02 {
    // 第三步：创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        // 创建线程
        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.increment01(); // +1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.decrement01(); // -1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();

        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.increment01(); // +1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CC").start();

        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.decrement01(); // -1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DD").start();
    }
}
