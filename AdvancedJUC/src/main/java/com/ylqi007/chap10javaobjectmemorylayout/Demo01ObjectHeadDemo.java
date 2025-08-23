package com.ylqi007.chap10javaobjectmemorylayout;

/**
 * @Author: ylqi007
 * @Date: 8/23/25  9:30 AM
 * @Description:
 */
public class Demo01ObjectHeadDemo {
    public static void main(String[] args) {
        Object o = new Object();    // new 一个对象，占多少内存
        System.out.println(o.hashCode());   // 1555009629，hashcode 记录在何处？

        synchronized (o) {
            System.out.println(o.hashCode());
        }

        System.gc();    // 手动 GC，。。。，15次之后，升级到养老区
    }
}

// Mark word (8 bytes) + Class Pointer (8 bytes)
class Customer {
    private int id;         // 4 bytes
    private boolean flag;   // 1 byte
}

// 16 + 4 + 1 = 21 bytes
// 3 bytes padding
// ==> 21 + 3 = 24 bytes