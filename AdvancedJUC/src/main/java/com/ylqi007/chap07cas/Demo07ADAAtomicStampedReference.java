package com.ylqi007.chap07cas;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  10:16 PM
 * @Description:
 */
public class Demo07ADAAtomicStampedReference {


    public static void main(String[] args) {
        // test01ABAProblem();

        test01ABASolveWithStamp();

    }


    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static void test01ABAProblem() {
        new Thread(() -> {
            // 模拟 ABA
            atomicInteger.compareAndSet(0, 1);
            CommonUtils.sleepMiliseconds(10);
            atomicInteger.compareAndSet(1, 0);
        }, "t-A").start();


        new Thread(() -> {
            CommonUtils.sleepMiliseconds(500);

            System.out.println(atomicInteger.compareAndSet(0, 2025) + "\t" + atomicInteger.get());
        }, "t-B").start();
    }


    private static AtomicStampedReference<Integer>  atomicStampedReference = new AtomicStampedReference<>(0, 1);
    private static void test01ABASolveWithStamp() {
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t首次版本号：" +  "\t" + atomicStampedReference.getStamp());

            // 暂停500ms，保证后面的 t-B 线程初始化拿到和 t-A 一样的版本号
            CommonUtils.sleepMiliseconds(500);

            atomicStampedReference.compareAndSet(0, 1, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t2次版本号：" + atomicStampedReference.getStamp());

            atomicStampedReference.compareAndSet(1, 0, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t3次版本号：" + atomicStampedReference.getStamp());
        }, "t-A").start();


        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t首次版本号：" + "\t" + atomicStampedReference.getStamp());

            // 等待 t-A 线程完成 ABA 问题
            CommonUtils.sleepSeconds(1);

            boolean b = atomicStampedReference.compareAndSet(0, 1, stamp, stamp + 1);
            System.out.println(b + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());

        }, "t-B").start();
    }
}
