package com.ylqi007.chap09threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: ylqi007
 * @Date: 8/21/25  10:46 PM
 * @Description:
 *
 * Without threadLocal.remove() ==> 数据会积累，可能线程爆 ==> 线程池，是复用的，保留了上次使用该线程的本地数据
 * pool-1-thread-2	 beforeAdd=0	 afterAdd=1
 * pool-1-thread-1	 beforeAdd=0	 afterAdd=1
 * pool-1-thread-3	 beforeAdd=0	 afterAdd=1
 * pool-1-thread-3	 beforeAdd=1	 afterAdd=2
 * pool-1-thread-2	 beforeAdd=1	 afterAdd=2
 * pool-1-thread-1	 beforeAdd=1	 afterAdd=2
 * pool-1-thread-2	 beforeAdd=2	 afterAdd=3
 * pool-1-thread-1	 beforeAdd=2	 afterAdd=3
 * pool-1-thread-3	 beforeAdd=2	 afterAdd=3
 * pool-1-thread-2	 beforeAdd=3	 afterAdd=4
 */
public class Demo02 {
    public static void main(String[] args) {
        MyData myData = new MyData();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    try {
                        Integer beforeAdd = myData.integerThreadLocal.get();
                        myData.add();
                        Integer afterAdd = myData.integerThreadLocal.get();

                        System.out.println(Thread.currentThread().getName() + "\t beforeAdd=" + beforeAdd + "\t afterAdd=" + afterAdd);
                    } finally {
                        myData.integerThreadLocal.remove();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}


class MyData {
    ThreadLocal<Integer> integerThreadLocal = ThreadLocal.withInitial(() -> 0);

    public void add() {
        integerThreadLocal.set(integerThreadLocal.get() + 1);
    }
}