package com.ylqi007.chap06volatile;

import com.ylqi007.utils.CommonUtils;

/**
 * @Author: ylqi007
 * @Date: 8/17/25  11:38 PM
 * @Description:
 */
public class Demo06UseVolatile {
    private static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (flag) {
                // do something
            }
        }, "thread-T1").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> {
            flag = false;
        }, "thread-T2").start();
    }
}
