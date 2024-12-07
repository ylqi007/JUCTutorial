package com.atguigu;

/**
 * Description: 用户线程 vs 守护线程(Daemon)
 *  用户线程(isDaemon==false)：自定义线程。主程序结束，用户线程还在继续，JVM存活
 *  守护线程(isDaemon==true)：没有用户线程了，都是守护线程，JVM结束。
 */
public class DaemonThread {
    public static void main(String[] args) {
        Thread aa = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "::" + Thread.currentThread().isDaemon());
            while(true) {

            }
        }, "aa");
        aa.setDaemon(true); // 设置为守护线程，要在aa.start()之前设置。
        aa.start();

        System.out.println(Thread.currentThread().getName() + " is over");;
    }
}