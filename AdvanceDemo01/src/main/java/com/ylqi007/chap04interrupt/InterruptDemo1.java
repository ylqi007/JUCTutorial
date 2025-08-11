package com.ylqi007.chap04interrupt;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptDemo1 {

    public static void main(String[] args) {
        // interruptWithVolatile();

        // interruptWithVolatileWrong();

        // interruptWithAtomicBoolean();

        interruptWithThreadAPI();
    }


    /**
     * Method 1. 通过一个volatile变量实现
     */
    // volatile表示的变量具有可见性
    private static volatile boolean isStop = false; // 可见性。真实开发中，如果需要多线程安全可见性(而不是通过锁同步)，`volatile`必须保留
    private static void interruptWithVolatile() {
        new Thread(() -> {
            while(true) {
                if(isStop) {
                    System.out.println(Thread.currentThread().getName() + "\t isStop 被修改为 true，线程停止");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + "---- Hello volatile");
            }
        }, "T-1").start();

        CommonUtils.sleepMiliseconds(100);

        new Thread(() -> {
            isStop = true;
        }, "T-2").start();
    }

    private static boolean isStopWithoutVolatile = false; // ？？？,
    private static void interruptWithVolatileWrong() {
        new Thread(() -> {
            while(true) {
                if(isStopWithoutVolatile) {
                    // System.out.println(Thread.currentThread().getName() + "\t isStop 被修改为 true，线程停止");
                    break;
                }
                // System.out.println(Thread.currentThread().getName() + "---- Hello volatile");
            }
        }, "T-1").start();

        CommonUtils.sleepMiliseconds(100);

        new Thread(() -> {
            isStopWithoutVolatile = true;
        }, "T-2").start();
    }


    /**
     * Method 2. 通过 AtomicBoolean
     */
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private static void interruptWithAtomicBoolean() {
        new Thread(() -> {
            while(true) {
                if(atomicBoolean.get()) {
                    System.out.println(Thread.currentThread().getName() + "\t atomicBoolean 被修改为 true，线程停止");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + "---- Hello AtomicBoolean");
            }
        }, "T-1").start();

        CommonUtils.sleepMiliseconds(1);

        new Thread(() -> {
            atomicBoolean.set(true);
        }, "T-2").start();
    }


    /**
     * Method 3. 通过 Thread 类自带的中断 API 实例方法。
     * 在需要中断的线程中不断监听中断状态，一旦发生中断，就执行相应的中断处理业务逻辑stop线程。
     */
    private static void interruptWithThreadAPI() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t isInterrupted() 被修改为 true，线程停止");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + "---- Hello Thread Interrupte API");
            }
        }, "t1");
        t1.start();
        System.out.println("###### Thread t1::默认的 interrupt flag = " + t1.isInterrupted());

        CommonUtils.sleepMiliseconds(10);

        // t1.interrupt();  // 也t1自己中断
        new Thread(() -> {
            System.out.println("修改前 interrupt flag = " + t1.isInterrupted());
            t1.interrupt(); // 设置中断标识位为 true
            System.out.println("修改后 interrupt flag = " + t1.isInterrupted());
        }, "t2").start();
    }
}
