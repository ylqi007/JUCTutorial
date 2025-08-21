package com.ylqi007.chap08atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Author: ylqi007
 * @Date: 8/20/25  8:42 PM
 * @Description:
 * 以一种线程安全的方式操作非线程安全对象的某些字段。
 *
 * 需求：
 * 10个线程
 * 每个线程转账1000
 * 不使用 synchronized, 尝试使用 AtomicIntegerFieldUpdater 来实现。
 */
public class Demo04AtomicIntegerFieldUpdater {
    public static void main(String[] args) throws InterruptedException {
        // withSynchronized();
        withAtomicIntegerFieldUpdater();
    }

    private static void withSynchronized() throws InterruptedException {
        BankAccount account = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        account.add();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "thread-" + i).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t" + "result = " + account.money);;
    }

    private static void withAtomicIntegerFieldUpdater() throws InterruptedException {
        BankAccount account = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        account.transferMoney(account);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "thread-" + i).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t" + "result = " + account.volatileMoney);;
    }
}


class BankAccount {
    String bankName = "Chase";
    int money = 0;  // 要保证线程安全

    public synchronized void add() {
        money++;
    }

    volatile int volatileMoney = 0;
    // 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 newUpdater() 创建一个更新器，并且需要设置想要更新的类和属性
    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "volatileMoney");

    // 不加 synchronized，保证高性能原子性，局部微创小手术
    public void transferMoney(BankAccount bankAccount) {
        fieldUpdater.getAndIncrement(bankAccount);
    }

}