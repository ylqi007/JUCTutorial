package com.ylqi007.chap11synchronizedup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: ylqi007
 * @Date: 8/23/25  11:30 PM
 * @Description:
 */
public class Demo01NoLock {
    public static void main(String[] args) {
        testNoLock01();
    }

    /**
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0) ==> Mark Word ==> 最后三位：001，即无锁态
     *   8   4        (object header: class)    0x00000d68
     *  12   4        (object alignment gap)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * 0x0000000000000001 (non-biasable; age: 0) ==> Mark Word ==> 最后三位：001，即无锁态
     * new 出来的对象，如果没有调用 hashCode(),是不会记录 hash code的。
     *
     * 添加：System.out.println(object.hashCode());
     * java.lang.Object object internals:
     * OFF  SZ   TYPE DESCRIPTION               VALUE
     *   0   8        (object header: mark)     0x00000008efb84601 (hash: 0x08efb846; age: 0)
     *   8   4        (object header: class)    0x00000d68
     *  12   4        (object alignment gap)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * 10进制: 149928006
     *  2进制: 1000111011111011100001000110
     * 16进制: 8efb846
     */
    private static void testNoLock01() {
        Object object = new Object();

        System.out.println("10进制: " + object.hashCode());
        System.out.println(" 2进制: " + Integer.toBinaryString(object.hashCode()));
        System.out.println("16进制: " + Integer.toHexString(object.hashCode()));

        System.out.println(ClassLayout.parseInstance(object).toPrintable());
    }
}
