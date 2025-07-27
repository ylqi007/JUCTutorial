package com.ylqi007.communication;


public class ThreadDemo01 {
    // 第三步：创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        // 创建线程
        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.increment(); // +1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                try {
                    sharedResource.decrement(); // -1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();
    }
}
