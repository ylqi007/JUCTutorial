package com.ylqi007.chap07cas;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  9:55 PM
 * @Description:
 * 题目：实现一个自旋锁，复习 CAS 思想
 * 通过 CAS 操作完成自旋锁，A线程先进来调用 myLock() 方法，自己持有锁5秒钟，B随后进来发现，当前线程持有锁，所以只能通过自旋等待，直到A线程释放锁后，B随后抢到
 */
public class Demo05SpinLock {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public static void main(String[] args) {
        Demo05SpinLock demo04SpinLock = new Demo05SpinLock();

        new Thread(() -> {
            demo04SpinLock.lock();
            CommonUtils.sleepSeconds(5);
            demo04SpinLock.unlock();
        }, "t-A").start();

        // Sleep 500 milliseconds to make sure thread t1 starts first
        CommonUtils.sleepMiliseconds(500);

        new Thread(() -> {
            demo04SpinLock.lock();

            demo04SpinLock.unlock();
        }, "t-B").start();
    }

    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t" + " come in");
        while(!atomicReference.compareAndSet(null, thread)) {

        }

    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t" + " task over, unlock");
        atomicReference.compareAndSet(thread, null);
    }
}
