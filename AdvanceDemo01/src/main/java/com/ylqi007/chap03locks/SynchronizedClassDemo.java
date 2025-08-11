package com.ylqi007.chap03locks;

public class SynchronizedClassDemo {
    Object object = new Object();


    public static void main(String[] args) {

    }

    /**
     * javap -c SynchronizedDemo
     *        5: astore_1
     *        6: monitorenter
     *        7: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
     *       10: invokestatic  #19                 // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
     *       13: invokevirtual #25                 // Method java/lang/Thread.getName:()Ljava/lang/String;
     *       16: invokedynamic #29,  0             // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;
     *       21: invokevirtual #33                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *       24: aload_1
     *       25: monitorexit
     *       26: goto          34
     *       29: astore_2
     *       30: aload_1
     *       31: monitorexit
     *       32: aload_2
     *       33: athrow
     *       34: return
     *
     * monitorenter + monitorexit(正常退出时可以释放锁) + monitorexit(抛出异常时可以释放锁)
     */
    public void method1() {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + ":: hello from synchronized code block.");
        }
    }


    /**
     * javap -v SynchronizedDemo
     *
     *   public synchronized void method2();
     *     descriptor: ()V
     *     flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED
     *     Code:
     *       stack=2, locals=1, args_size=1
     *          0: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
     *          3: invokestatic  #19                 // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
     *          6: invokevirtual #25                 // Method java/lang/Thread.getName:()Ljava/lang/String;
     *          9: invokedynamic #39,  0             // InvokeDynamic #1:makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;
     *         14: invokevirtual #33                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *         17: return
     *       LineNumberTable:
     *         line 40: 0
     *         line 41: 17
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0      18     0  this   Lcom/ylqi007/chap03locks/SynchronizedDemo;
     *
     * ACC_SYNCHRONIZED ==> 对象锁
     */
    public synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + ":: hello from synchronized method.");
    }


    /**
     * javap -v SynchronizedDemo
     *
     *   public static synchronized void method3();
     *     descriptor: ()V
     *     flags: (0x0029) ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
     *     Code:
     *       stack=2, locals=0, args_size=0
     *          0: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
     *          3: invokestatic  #19                 // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
     *          6: invokevirtual #25                 // Method java/lang/Thread.getName:()Ljava/lang/String;
     *          9: invokedynamic #40,  0             // InvokeDynamic #2:makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;
     *         14: invokevirtual #33                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *         17: return
     *       LineNumberTable:
     *         line 71: 0
     *         line 72: 17
     *
     * ACC_STATIC, ACC_SYNCHRONIZED ==> 类锁
     */
    public static synchronized void method3() {
        System.out.println(Thread.currentThread().getName() + ":: hello from static synchronized method.");
    }
}
