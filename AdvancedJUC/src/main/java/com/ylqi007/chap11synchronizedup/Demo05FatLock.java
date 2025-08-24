package com.ylqi007.chap11synchronizedup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  8:52 AM
 * @Description:
 *
 * # WARNING: Unable to get Instrumentation. Dynamic Attach failed. You may add this JAR as -javaagent manually, or supply -Djdk.attach.allowAttachSelf
 * # WARNING: Unable to attach Serviceability Agent. You can try again with escalated privileges. Two options: a) use -Djol.tryWithSudo=true to try with sudo; b) echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000600001e740d2 (fat lock: 0x0000600001e740d2)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000600001e740d2 (fat lock: 0x0000600001e740d2)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000600001e740d2 (fat lock: 0x0000600001e740d2)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000600001e740d2 (fat lock: 0x0000600001e740d2)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 *
 * java.lang.Object object internals:
 * OFF  SZ   TYPE DESCRIPTION               VALUE
 *   0   8        (object header: mark)     0x0000600001e740d2 (fat lock: 0x0000600001e740d2)
 *   8   4        (object header: class)    0x00000d68
 *  12   4        (object alignment gap)
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
public class Demo05FatLock {
    public static void main(String[] args) {
        // Biased
        Object lock = new Object();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (lock) {
                    System.out.println(ClassLayout.parseInstance(lock).toPrintable());
                }
            }, "t" + i).start();
        }
    }
}
