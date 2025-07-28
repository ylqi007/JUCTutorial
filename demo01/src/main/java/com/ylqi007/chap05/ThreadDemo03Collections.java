package com.ylqi007.chap05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class ThreadDemo03Collections {
    public static void main(String[] args) {
        // 创建ArrayList
        // List<String> list = new ArrayList<>();
        // List<String> list = new Vector<>();

        List<String> list = Collections.synchronizedList(new ArrayList<>());
        // 演示并发修改异常
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                list.add(UUID.randomUUID().toString().substring(0, 8));

                // 从集合中获取内容
                System.out.println(list);   // Exception in thread "Thread-7" java.util.ConcurrentModificationException
            }, "Thread-" + i).start();
        }
    }
}
