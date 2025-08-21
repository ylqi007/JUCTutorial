package com.ylqi007.chap08atomic;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @Author: ylqi007
 * @Date: 8/20/25  8:21 PM
 * @Description:
 */
public class Demo03AtomicMarkableReference {
    private static AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference<>(100, false);

    public static void main(String[] args) {
        new Thread(() -> {
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 默认标识：" + marked);

            // 暂停1s，等待线程2拿到和线程1一样的 marked 值，都是false
            CommonUtils.sleepSeconds(1);

            atomicMarkableReference.compareAndSet(100, 1000, marked, !marked);
        }, "t-1").start();

        new Thread(() -> {
            boolean marked = atomicMarkableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 默认标识：" + marked);

            CommonUtils.sleepSeconds(2);

            boolean b = atomicMarkableReference.compareAndSet(100, 2000, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t Result = " + b);
            System.out.println(Thread.currentThread().getName() + "\t" + atomicMarkableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "\t" + atomicMarkableReference.getReference());
        }, "t-2").start();
    }

    public static AtomicMarkableReference<Integer> getAtomicMarkableReference() {
        return atomicMarkableReference;
    }

    public static void setAtomicMarkableReference(AtomicMarkableReference<Integer> atomicMarkableReference) {
        Demo03AtomicMarkableReference.atomicMarkableReference = atomicMarkableReference;
    }
}
