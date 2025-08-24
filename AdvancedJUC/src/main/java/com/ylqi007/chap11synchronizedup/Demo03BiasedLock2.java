package com.ylqi007.chap11synchronizedup;

import com.ylqi007.utils.CommonUtils;
import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/24/25  12:27 AM
 * @Description:
 * VM Option: -XX:+UseBiasedLocking
 */
public class Demo03BiasedLock2 {
    public static void main(String[] args) {
        // Biased
        Object lock = new Object();

        // 暂停25s
        CommonUtils.sleepSeconds(5);

        /**
         * OpenJDK 64-Bit Server VM warning: Option UseBiasedLocking was deprecated in version 15.0 and will likely be removed in a future release.
         * # WARNING: Unable to get Instrumentation. Dynamic Attach failed. You may add this JAR as -javaagent manually, or supply -Djdk.attach.allowAttachSelf
         * # WARNING: Unable to attach Serviceability Agent. You can try again with escalated privileges. Two options: a) use -Djol.tryWithSudo=true to try with sudo; b) echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope
         * java.lang.Object object internals:
         * OFF  SZ   TYPE DESCRIPTION               VALUE
         *   0   8        (object header: mark)     0x0000000152008905 (biased: 0x0000000000548022; epoch: 0; age: 0)
         *   8   4        (object header: class)    0x00000d68
         *  12   4        (object alignment gap)
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */
        synchronized (lock) {
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }
    }
}
