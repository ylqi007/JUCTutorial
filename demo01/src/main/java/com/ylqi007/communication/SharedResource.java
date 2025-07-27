package com.ylqi007.communication;

// 第一步：资源类
public class SharedResource {
    private int count = 0;

    // +1操作，if-statement
    public synchronized void increment() throws InterruptedException {
        // 第二步：判断，干活，通知
        if(count != 0) {
            this.wait();
        }

        // 如果count为0，执行+1操作
        count++;
        System.out.println(Thread.currentThread().getName() + "::" + count);

        // 通知其他线程
        this.notifyAll();
    }

    // -1操作，if-statement
    public synchronized void decrement() throws InterruptedException {
        if(count == 0) {
            this.wait();
        }

        // 如果count为1，执行-1操作
        count--;
        System.out.println(Thread.currentThread().getName() + "::" + count);

        // 通知其他线程
        this.notifyAll();
    }

    // +1操作，while-statement
    public synchronized void increment01() throws InterruptedException {
        // 第二步：判断，干活，通知
        while(count != 0) {
            this.wait();
        }

        // 如果count为0，执行+1操作
        count++;
        System.out.println(Thread.currentThread().getName() + "::" + count);

        // 通知其他线程
        this.notifyAll();
    }

    // -1操作，while-statement
    public synchronized void decrement01() throws InterruptedException {
        while(count == 0) {
            this.wait();
        }

        // 如果count为1，执行-1操作
        count--;
        System.out.println(Thread.currentThread().getName() + "::" + count);

        // 通知其他线程
        this.notifyAll();
    }
}