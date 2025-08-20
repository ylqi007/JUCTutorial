package com.ylqi007.chap05;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThreadDemo01ArrayList {
    public static void main(String[] args) {
        // 创建ArrayList
        List<String> list = new ArrayList<>();

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

/*
[7bfcd80c, cc5e7282]
[7bfcd80c, cc5e7282, 16352aab, 27643a12, 67456328, 15ebd2cc]
[7bfcd80c, cc5e7282, 16352aab, 27643a12, 67456328, 15ebd2cc, 735aef6d]
[7bfcd80c, cc5e7282, 16352aab, 27643a12]
[7bfcd80c, cc5e7282, 16352aab, 27643a12, 67456328]
[7bfcd80c, cc5e7282, 16352aab]
[7bfcd80c, cc5e7282]
[7bfcd80c, cc5e7282, 16352aab, 27643a12, 67456328, 15ebd2cc, 735aef6d, f570950f, c1d61eb0, e5510205]
[7bfcd80c, cc5e7282, 16352aab, 27643a12, 67456328, 15ebd2cc, 735aef6d, f570950f]
Exception in thread "Thread-7" java.util.ConcurrentModificationException
	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1013)
	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:967)
	at java.base/java.util.AbstractCollection.toString(AbstractCollection.java:456)
	at java.base/java.lang.String.valueOf(String.java:4220)
	at java.base/java.io.PrintStream.println(PrintStream.java:1047)
	at com.ylqi007.chap05.ThreadDemo01.lambda$main$0(ThreadDemo01.java:19)
	at java.base/java.lang.Thread.run(Thread.java:840)
 */
