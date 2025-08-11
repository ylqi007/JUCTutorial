package com.ylqi007.chap04locksupport;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 去掉 lock/unlock ，会抛出异常
 */
public class Demo02LockConditionAwaitSignal {
    public static void main(String[] args) {
        //  normalProcedure();

        // testException01();

        testException02();
    }

    /**
     * t1	 ---- come in
     * t2	 -- 发出唤醒通知
     * t1	 ---- 被唤醒
     */
    private static void normalProcedure() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * t1	 ---- come in
     * Exception in thread "t1" java.lang.IllegalMonitorStateException
     * 	at java.base/java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.enableWait(AbstractQueuedSynchronizer.java:1521)
     * 	at java.base/java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:1618)
     * 	at com.ylqi007.chap04locksupport.Demo02LockConditionAwaitSignal.lambda$testException01$2(Demo02LockConditionAwaitSignal.java:59)
     * 	at java.base/java.lang.Thread.run(Thread.java:840)
     * Exception in thread "t2" java.lang.IllegalMonitorStateException
     * 	at java.base/java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.signal(AbstractQueuedSynchronizer.java:1478)
     * 	at com.ylqi007.chap04locksupport.Demo02LockConditionAwaitSignal.lambda$testException01$3(Demo02LockConditionAwaitSignal.java:73)
     * 	at java.base/java.lang.Thread.run(Thread.java:840)
     */
    private static void testException01() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            // lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");
                condition.await(); // Throws exception
                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //lock.unlock();
            }
        }, "t1").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            //lock.lock();
            try {
                condition.signal(); // Throws exception
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            } finally {
                //lock.unlock();
            }
        }, "t2").start();
    }


    /**
     * t2	 -- 发出唤醒通知
     * t1	 ---- come in
     * 阻塞，
     */
    private static void testException02() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            CommonUtils.sleepSeconds(1);

            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t ---- come in");
                condition.await(); // Throws exception
                System.out.println(Thread.currentThread().getName() + "\t ---- 被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        // CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal(); // Throws exception
                System.out.println(Thread.currentThread().getName() + "\t -- 发出唤醒通知");
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
