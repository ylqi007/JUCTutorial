package com.ylqi007.chap11synchronizedup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  12:14 AM
 * @Description: 锁升级
 *
 * 注意添加 VM Option：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
 */
public class Demo03BiasedLock {
    public static void main(String[] args) {
        // Biased
        Object lock = new Object();

        /**
         * With: -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
         * java.lang.Object object internals:
         * OFF  SZ   TYPE DESCRIPTION               VALUE
         *   0   8        (object header: mark)     0x0000000131009105 (biased: 0x00000000004c4024; epoch: 0; age: 0)
         *   8   4        (object header: class)    0x00000d68
         *  12   4        (object alignment gap)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         *
         * 0x0000000131009105 (biased: 0x00000000004c4024; epoch: 0; age: 0)
         *  ==> 5 ==> 101 ==> biased, 即偏向锁
         */
        synchronized (lock) {
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }
    }
}
