package com.ylqi007.chap08atomic;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @Author: ylqi007
 * @Date: 8/20/25  8:58 PM
 * @Description:
 *
 * 需求：
 * 多线程并发调用一个类的初始化方法，如果未被初始化过，将执行初始化工作。
 * 要求只能被初始化一次，只有一个线程操作成功。
 */
public class Demo05AtomicReferenceFieldUpdater {
    public static void main(String[] args) {
        MyResource myResource = new MyResource();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                myResource.init(myResource);
            }, "Thread-" + i).start();
        }
    }
}


// 资源类
class MyResource {
    public volatile Boolean isInit = Boolean.FALSE;

    AtomicReferenceFieldUpdater<MyResource, Boolean> referenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyResource.class, Boolean.class, "isInit");

    public void init(MyResource myResource) {
        if (myResource.referenceFieldUpdater.compareAndSet(myResource, Boolean.FALSE, Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- start init(), needs 2 seconds");
            CommonUtils.sleepSeconds(2);
            System.out.println(Thread.currentThread().getName() + "\t" + "---- end init()");
        } else {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- 已经有线程在进行初始化操作");
        }


    }

}

