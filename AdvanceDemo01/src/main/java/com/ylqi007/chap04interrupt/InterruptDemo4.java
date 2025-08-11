package com.ylqi007.chap04interrupt;

public class InterruptDemo4 {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); // false
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); // false

        System.out.println("---- 1");
        Thread.currentThread().interrupt(); // 被执行了
        System.out.println("---- 2");

        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); // true： 先返回true，在清理中断状态
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted()); // false

        Thread.interrupted();   // static
        Thread.currentThread().isInterrupted(); // instance method
    }
}
