package com.ylqi007.chap05;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadDemo06CopyOnWriteArraySet {
    public static void main(String[] args) {
        // Set<String> set = new HashSet<>();
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                set.add(Thread.currentThread().getName() + "-" + UUID.randomUUID().toString().substring(0, 8));

                // 从set中获取内容
                System.out.println(set);    // Exception in thread "Thread-2" Exception in thread "Thread-4" java.util.ConcurrentModificationException
            }, "Thread-" + i).start();
        }
    }
}
