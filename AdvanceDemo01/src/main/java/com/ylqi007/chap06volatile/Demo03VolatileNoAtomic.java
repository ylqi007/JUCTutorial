package com.ylqi007.chap06volatile;

import com.ylqi007.utils.CommonUtils;

/**
* @Author: ylqi007
* @Date: 8/13/25  9:57 PM
* @Description: 
*/
public class Demo03VolatileNoAtomic {
    public static void main(String[] args) {

        testWithVolatile();
    }

    /**
     * 10000
     */
    private static void testWithSynchronized() {
        MyNumber myNumber = new MyNumber();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myNumber.increment();
                }
            }, "thread-" + i).start();
        }

        CommonUtils.sleepSeconds(1);

        System.out.println(myNumber.number);
    }

    /**
     * 偶尔出现非10000的结果，比如 9941
     * 说明 volatile 并不具备原子性
     */
    private static void testWithVolatile() {
        MyNumber1 myNumber1 = new MyNumber1();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myNumber1.increment();
                }
            }, "thread-" + i).start();
        }

        CommonUtils.sleepSeconds(1);

        System.out.println(myNumber1.number);
    }

}


class MyNumber {
    int number;

    public synchronized void increment() {
        number++;
    }
}

class MyNumber1 {
    volatile int number;

    public void increment() {
        number++;
    }
}