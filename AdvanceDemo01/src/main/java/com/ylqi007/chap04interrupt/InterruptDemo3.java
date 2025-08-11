package com.ylqi007.chap04interrupt;

import com.ylqi007.utils.CommonUtils;


/**
 * 1. 中断标识位，默认 false
 * 2. t2 线程调用 t1.interrupt()，对 t1 发出中断协商，中断标识位变为 true
 * 3. 中断标识位为 true，正常情况，程序停止
 * 4. 中断标识位为 true，异常情况，InterruptedException，将会把中断状态清除，并且将收到 InterruptedException，中断标识位为 false，导致死循环
 * 5. 在 catch 块中，需要再次给中断标识位设置为 true，2 次调用，才停止程序
 */
public class InterruptDemo3 {

    public static void main(String[] args) {
        // 死循环
        testInterruptException();

        // testInterruptException2();
    }


    /**
     * ---- Hello InterruptDemo03
     * ---- Hello InterruptDemo03
     * java.lang.InterruptedException: sleep interrupted
     * 	at java.base/java.lang.Thread.sleep(Native Method)
     * 	at java.base/java.lang.Thread.sleep(Thread.java:344)
     * 	at java.base/java.util.concurrent.TimeUnit.sleep(TimeUnit.java:446)
     * 	at com.ylqi007.utils.CommonUtils.sleepMiliseconds(CommonUtils.java:56)
     * 	at com.ylqi007.chap04interrupt.InterruptDemo03.lambda$main$0(InterruptDemo03.java:13)
     * 	at java.base/java.lang.Thread.run(Thread.java:840)
     * ---- Hello InterruptDemo03
     * ---- Hello InterruptDemo03
     */
    private static void testInterruptException() {
        Thread t1 = new Thread(() -> {
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " 中断标识位 = " + Thread.currentThread().isInterrupted() + " ----  程序终止");
                    break;
                }
                //sleep方法抛出InterruptedException后，中断标识也被清空置为false，如果没有在
                //catch方法中调用interrupt方法再次将中断标识置为true，这将导致无限循环了
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // Thread.currentThread().interrupt(); // 捕获到 InterruptedException 后，再次设置标识位
                    e.printStackTrace();
                }
                System.out.println("---- Hello InterruptDemo03");
            }
        }, "t1");
        t1.start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> t1.interrupt(), "t2").start();
    }

    private static void testInterruptException2() {
        Thread t1 = new Thread(() -> {
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " 中断标识位 = " + Thread.currentThread().isInterrupted() + " ----  程序终止");
                    break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 捕获到 InterruptedException 后，再次设置标识位
                    e.printStackTrace();
                }
                System.out.println("---- Hello InterruptDemo03");
            }
        }, "t1");
        t1.start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> t1.interrupt(), "t2").start();
    }
}
