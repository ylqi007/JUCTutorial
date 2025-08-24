package com.ylqi007.chap11synchronizedup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  12:25 AM
 * @Description: 演示轻量锁
 */
public class Demo04ThinLock {
    public static void main(String[] args) {
        // Biased
        Object lock = new Object();

        /**
         * java.lang.Object object internals:
         * OFF  SZ   TYPE DESCRIPTION               VALUE
         *   0   8        (object header: mark)     0x000000016f862b00 (thin lock: 0x000000016f862b00)
         *   8   4        (object header: class)    0x00000d68
         *  12   4        (object alignment gap)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         *
         * 0x000000016f862b00 (thin lock: 0x000000016f862b00)
         *  ==> 最后一个十六进制位为0 ==> 0000 ==> thin lock
         */
        new Thread(() -> {
            synchronized (lock) {
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
        }, "t1").start();
    }
}
