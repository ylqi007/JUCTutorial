package com.ylqi007.chap05;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ThreadDemo05HashSet {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                set.add(Thread.currentThread().getName() + UUID.randomUUID().toString().substring(0, 8));

                // 从set中获取内容
                System.out.println(set);    // Exception in thread "Thread-2" Exception in thread "Thread-4" java.util.ConcurrentModificationException
            }, "Thread-" + i).start();
        }
    }
}

/*
[Thread-0aa945c75, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5]
[Thread-0aa945c75, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5]
[Thread-0aa945c75, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5]
[Thread-1a08e7341, Thread-661652186, Thread-0aa945c75, Thread-46f2d6383, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5, Thread-2e5a0afb6, Thread-8e78c3340]
[Thread-0aa945c75, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5]
[Thread-0aa945c75, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5]
[Thread-1a08e7341, Thread-0aa945c75, Thread-46f2d6383, Thread-99b21f2a7, Thread-733dde4c9, Thread-58a43d3f5, Thread-2e5a0afb6]
Exception in thread "Thread-2" Exception in thread "Thread-4" java.util.ConcurrentModificationException
	at java.base/java.util.HashMap$HashIterator.nextNode(HashMap.java:1597)
	at java.base/java.util.HashMap$KeyIterator.next(HashMap.java:1620)
	at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:456)
	at java.base/java.lang.String.valueOf(String.java:4220)
	at java.base/java.io.PrintStream.println(PrintStream.java:1047)
	at com.ylqi007.chap05.ThreadDemo05HashSet.lambda$main$0(ThreadDemo05HashSet.java:16)
	at java.base/java.lang.Thread.run(Thread.java:840)
java.util.ConcurrentModificationException
	at java.base/java.util.HashMap$HashIterator.nextNode(HashMap.java:1597)
	at java.base/java.util.HashMap$KeyIterator.next(HashMap.java:1620)
	at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:456)
	at java.base/java.lang.String.valueOf(String.java:4220)
	at java.base/java.io.PrintStream.println(PrintStream.java:1047)
	at com.ylqi007.chap05.ThreadDemo05HashSet.lambda$main$0(ThreadDemo05HashSet.java:16)
	at java.base/java.lang.Thread.run(Thread.java:840)
Exception in thread "Thread-8" java.util.ConcurrentModificationException
	at java.base/java.util.HashMap$HashIterator.nextNode(HashMap.java:1597)
	at java.base/java.util.HashMap$KeyIterator.next(HashMap.java:1620)
	at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:456)
	at java.base/java.lang.String.valueOf(String.java:4220)
	at java.base/java.io.PrintStream.println(PrintStream.java:1047)
	at com.ylqi007.chap05.ThreadDemo05HashSet.lambda$main$0(ThreadDemo05HashSet.java:16)
	at java.base/java.lang.Thread.run(Thread.java:840)
 */