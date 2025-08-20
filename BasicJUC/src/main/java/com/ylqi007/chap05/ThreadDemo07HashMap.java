package com.ylqi007.chap05;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadDemo07HashMap {
    public static void main(String[] args) {
        // Map<String, String> map = new HashMap<>();
        Map<String, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 向集合中添加内容
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));

                // 从set中获取内容
                System.out.println(map);    // Exception in thread "Thread-2" Exception in thread "Thread-4" java.util.ConcurrentModificationException
            }, "Thread-" + i).start();
        }
    }
}

/*
/Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home/bin/java -javaagent:/Users/ylqi007/Applications/IntelliJ IDEA Ultimate.app/Contents/lib/idea_rt.jar=63665:/Users/ylqi007/Applications/IntelliJ IDEA Ultimate.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/ylqi007/Work/JUCTutorial/demo01/target/classes:/Users/ylqi007/.m2/repository/org/apache/logging/log4j/log4j-core/2.24.3/log4j-core-2.24.3.jar:/Users/ylqi007/.m2/repository/org/apache/logging/log4j/log4j-api/2.24.3/log4j-api-2.24.3.jar com.ylqi007.chap05.ThreadDemo07HashMap
{Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-0=7a497d9b}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-9=1b04fcd1, Thread-0=7a497d9b, Thread-1=e429301b, Thread-2=1399db62}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-9=1b04fcd1, Thread-0=7a497d9b, Thread-2=1399db62}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-9=1b04fcd1, Thread-0=7a497d9b}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-8=df06bc4d, Thread-9=1b04fcd1, Thread-0=7a497d9b, Thread-1=e429301b, Thread-2=1399db62}
{Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-0=7a497d9b}
{Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-0=7a497d9b}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-7=3c541f64, Thread-8=df06bc4d, Thread-9=1b04fcd1, Thread-0=7a497d9b, Thread-1=e429301b, Thread-2=1399db62}
{Thread-3=b4681041, Thread-4=6cf2cb56, Thread-5=15dbb984, Thread-6=25f94c8a, Thread-7=3c541f64, Thread-8=df06bc4d, Thread-9=1b04fcd1, Thread-0=7a497d9b, Thread-1=e429301b, Thread-2=1399db62}
Exception in thread "Thread-9" java.util.ConcurrentModificationException
	at java.base/java.util.HashMap$HashIterator.nextNode(HashMap.java:1597)
	at java.base/java.util.HashMap$EntryIterator.next(HashMap.java:1630)
	at java.base/java.util.HashMap$EntryIterator.next(HashMap.java:1628)
	at java.base/java.util.AbstractMap.toString(AbstractMap.java:550)
	at java.base/java.lang.String.valueOf(String.java:4220)
	at java.base/java.io.PrintStream.println(PrintStream.java:1047)
	at com.ylqi007.chap05.ThreadDemo07HashMap.lambda$main$0(ThreadDemo07HashMap.java:17)
	at java.base/java.lang.Thread.run(Thread.java:840)
 */