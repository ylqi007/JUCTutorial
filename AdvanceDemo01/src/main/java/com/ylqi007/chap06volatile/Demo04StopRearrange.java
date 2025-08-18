package com.ylqi007.chap06volatile;

/**
 * @Author: ylqi007
 * @Date: 8/13/25  10:23 PM
 * @Description:
 */
public class Demo04StopRearrange {
    public static void main(String[] args) {

    }

    private static int i = 0;
    private static volatile boolean flag = false;   // 添加 volatile，禁止指令重排
    private static void write() {
        i = 2;          // 普通写
        flag = true;    // volatile 写
    }

    private static void read() {
        if(flag) {  // volatile read
            System.out.println("#### i = " + i);
        }
    }
}
