package com.ylqi007.chap04interrupt;

import com.ylqi007.utils.CommonUtils;

/**
 * 如果线程处于正常活动状态，那么 interrupt() 方法将该线程的中断标识位设置为 true，仅此而已，被设置中断标识位的线程继续正常运行，不受影响。
 * 所以，interrupt() 并不能真正地中断线程，需要被调用对象的线程自己进行配合才行，对于不活动的线程没有任何影响。
 */
public class InterruptDemo2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                System.out.println(Thread.currentThread().getName() + ":\t" + i);
            }
            System.out.println(Thread.currentThread().getName() + ":: t1 线程调用 t1.interrupt() 后的中断标识位 02 = " + Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        System.out.println(Thread.currentThread().getName() + ":: t1 线程的默认中断标识位 = " + t1.isInterrupted());

        CommonUtils.sleepMiliseconds(2);
        t1.interrupt(); // Set interrupt flag to true

        System.out.println(Thread.currentThread().getName() + ":: t1 线程调用 t1.interrupt() 后的中断标识位 01 = " + t1.isInterrupted());   // true

        CommonUtils.sleepSeconds(2);
        // JDK17 不会自动恢复中断标识位
        System.out.println(Thread.currentThread().getName() + ":: t1 线程调用 t1.interrupt() 后的中断标识位 03 = " + t1.isInterrupted());    // true? 视频中为 false
    }
}
