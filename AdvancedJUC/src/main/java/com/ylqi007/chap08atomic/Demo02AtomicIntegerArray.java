package com.ylqi007.chap08atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Author: ylqi007
 * @Date: 8/20/25  6:21 PM
 * @Description:
 */
public class Demo02AtomicIntegerArray {
    public static void main(String[] args) {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);

        for(int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(atomicIntegerArray.get(i));
        }

        System.out.println("==========");

        int tmpInt = 0;

        tmpInt = atomicIntegerArray.getAndSet(0, 2025);
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(0));

        tmpInt = atomicIntegerArray.getAndIncrement(0);
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(0));
    }
}
