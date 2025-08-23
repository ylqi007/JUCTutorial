package com.ylqi007.chap10javaobjectmemorylayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @Author: ylqi007
 * @Date: 8/23/25  9:57 AM
 * @Description: 使用 JOL
 *
 * -XX:MaxTenuringThreshold=16 ==> JDK17 运行16了
 */
public class Demo02JOL {
    public static void main(String[] args) {
        // Thread.currentThread()
//        System.out.println(VM.current().details());
//
//        System.out.println(VM.current().objectAlignment());

        Object o = new Object();    // Instance size: 16 bytes
         System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // Instance size: 16 bytes, 只有 object header，没有 instance data

//        JOLCustomer jolCustomer = new JOLCustomer();
//        System.out.println(ClassLayout.parseInstance(jolCustomer).toPrintable());
    }
}

/**
 * com.ylqi007.chap10javaobjectmemorylayout.JOLCustomer object internals:
 * OFF  SZ      TYPE DESCRIPTION               VALUE
 *   0   8           (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
 *   8   4           (object header: class)    0x010cd868
 *  12   4       int JOLCustomer.id            0
 *  16   1   boolean JOLCustomer.flag          false
 *  17   7           (object alignment gap)
 * Instance size: 24 bytes
 * Space losses: 0 bytes internal + 7 bytes external = 7 bytes total
 */
class JOLCustomer {
    // 第一种情况：只有对象头

    // 第二种情况：有 instance data
    int id;
    boolean flag;
}
