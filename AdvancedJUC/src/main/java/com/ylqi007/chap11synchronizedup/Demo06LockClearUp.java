package com.ylqi007.chap11synchronizedup;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  9:10 AM
 * @Description: 锁消除
 */
public class Demo06LockClearUp {
    static Object lock = new Object();

    public static void main(String[] args) {
        Demo06LockClearUp demo06LockClearUp = new Demo06LockClearUp();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                demo06LockClearUp.m1();
            }, "t" + i).start();
        }
    }

    private void m1() {
//        synchronized (lock) {
//            System.out.println(Thread.currentThread().getName() + " Hello World");
//        }

        // 锁消除问题： JIT 编译器会无视它，synchronized (o)，每次 new 出来的，不存在了，非常正常。
        // 多个线程抢一把锁
        // 此处是每个线程一把锁
        Object o = new Object();
        synchronized (o) {
            System.out.println(Thread.currentThread().getName() + "\t" + o.hashCode() + "\t" + lock.hashCode());
        }
    }
}
