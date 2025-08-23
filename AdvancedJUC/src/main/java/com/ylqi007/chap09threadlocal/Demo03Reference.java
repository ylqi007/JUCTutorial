package com.ylqi007.chap09threadlocal;

import com.ylqi007.utils.CommonUtils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ylqi007
 * @Date: 8/21/25  11:12 PM
 * @Description:
 */
public class Demo03Reference {
    public static void main(String[] args) {
        // testStringReference();

        // testSoftReference();

        // testWeakReference();

        testPhantomReference();
    }

    private static void testStringReference() {
        MyObject myObject = new MyObject();

        System.out.println("Before GC: " + myObject);

        myObject = null;
        System.gc();    // 主动触发GC，一般不用

        System.out.println("After GC: " + myObject);
    }

    /**
     * SoftReference:
     *  * 当系统内存充足时，不会被回收
     *  * 当系统内存不足时，会被回收
     * 设置 VM Options: -Xms10m -Xmx10m
     * ---- Before GC:: softReference: java.lang.ref.SoftReference@8efb846
     * ---- After GC (内存够用):: softReference: com.ylqi007.chap09threadlocal.MyObject@a09ee92
     * ---- invoke finalize method ----
     * ---- After GC (内存不够用了！！！):: softReference: null
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at com.ylqi007.chap09threadlocal.Demo03Reference.testSoftReference(Demo03Reference.java:48)
     * 	at com.ylqi007.chap09threadlocal.Demo03Reference.main(Demo03Reference.java:16)
     */
    private static void testSoftReference() {
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());

        System.out.println("---- Before GC:: softReference: " + softReference);

        System.gc();

        CommonUtils.sleepSeconds(1);

        System.out.println("---- After GC (内存够用):: softReference: " + softReference.get());

        try {
            byte[] bytes = new byte[20 * 1024 * 1024];  // 20MB
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---- After GC (内存不够用了！！！):: softReference: " + softReference.get());
        }
    }

    /**
     * 弱引用需要 java.lang.ref.WeakReference 类来实现，它比软引用的生存时期更短。
     * 对于只有弱引用的对象来说，只要垃圾回收机制一运行，不管 JVM 的内存空间是否足够，都会回收该对象占用的内存
     *
     * ---- Before GC:: weakReference: java.lang.ref.WeakReference@8efb846
     * ---- invoke finalize method ----
     * ---- After GC (内存够用):: weakReference: null
     */
    private static void testWeakReference() {
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());

        System.out.println("---- Before GC:: weakReference: " + weakReference);

        System.gc();

        CommonUtils.sleepSeconds(1);

        System.out.println("---- After GC (内存够用):: weakReference: " + weakReference.get());
    }

    /**
     * 设置
     * PhantomReference.get() 总是返回 null，无法访问对象
     *
     * > 这里invoke日志的打印不是byte数组内对象回收，就是哪个myobject被回收了，finalize是回收之前，之后虚引用就被丢到队列里面去了，就有最后的“虚对象加入队列”的日志???
     */
    private static void testPhantomReference() {
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();

        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject, referenceQueue);

        System.out.println("---- Before GC:: phantomReference: " + phantomReference.get()); // ---- Before GC:: phantomReference: null

        List<byte[]> list = new ArrayList<>();

        new Thread(() -> {
            while (true) {
                list.add(new byte[1 * 1024 * 1024]);    // 1MB
                CommonUtils.sleepMiliseconds(500);
                System.out.println(phantomReference.get() + "\t" + "list add OK");
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends MyObject> reference = referenceQueue.poll();
                if(reference != null) {
                    System.out.println("---- 有虚对象回收加入了队列");
                }
            }
        }, "t2").start();

    }
}


class MyObject {
    // 这个方法一般不重写，此处只是为了演示案例
    @Override
    protected void finalize() throws Throwable {
        // super.finalize();
        // finalize 的通常目的是在对象被不可撤销地丢弃之前执行清理操作
        System.out.println("---- invoke finalize method ----");
    }
}