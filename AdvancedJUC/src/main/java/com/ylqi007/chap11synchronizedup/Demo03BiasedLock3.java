package com.ylqi007.chap11synchronizedup;

import com.ylqi007.utils.CommonUtils;
import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  12:34 AM
 * @Description:
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x000000017eada940 (thin lock: 0x000000017eada940)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
public class Demo03BiasedLock3 {
    public static void main(String[] args) {
        // 暂停2s
        CommonUtils.sleepSeconds(2);

        // Biased
        Object lock = new Object();
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        new Thread(() -> {
            synchronized (lock) {
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
        }, "t1").start();
    }
}
