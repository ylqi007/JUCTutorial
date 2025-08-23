package com.ylqi007.chap09threadlocal;

import java.util.Random;

/**
 * @Author: ylqi007
 * @Date: 8/22/25  6:26 PM
 * @Description: 线程间隔离, 使每个线程拥有一个独享的对象, 保证线程安全
 *
 * 我们知道很多JDK中提供的类都是线程不安全的, 比如说Random类, 我们可以用它来产生随机数, 为什么他是线程不安全的呢? 因为他其实是使用种子来产生随机数的, 使用旧种子产生新种子, 那么在多线程的情况下, 因为程序的异步性, 就可能有多个线程拿到相同的旧种子, 从而产生相同的新种子, 如果是这样, 这就不能叫做随机数了.
 *
 * 作者：八股取士
 * 链接：https://leetcode.cn/discuss/post/RyJH2O/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class Demo04RandomTest {

    public static void main(String[] args) {
        testWithRandom();
    }

    private static void testWithRandom() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 生成随机数
                int randInt = rand.nextInt();
                // 打印
                System.out.println(Thread.currentThread().getName() + ": " + randInt);
            }, "t-" + i).start();
        }
    }


    // ThreadLocal对象, 内部存储Random对象
    private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(() -> new Random());
    private static void testWithThreadLocal() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 从ThreadLocal中取出Random对象
                Random rand = Demo04RandomTest.RANDOM.get();
                // 生成随机数
                int randInt = rand.nextInt();
                // 打印
                System.out.println(Thread.currentThread().getName() + ": " + randInt);
            }, "t-" + i).start();
        }
    }

}
