# Chap06. 多线程锁
在**多线程**环境下，为了让多线程安全地访问和使用**共享变量**，必须引入**锁机制**。锁机制即当一个线程持有锁后，其他线程只能进行等待，直到持有锁的的线程释放锁，再次重新竞争锁。

## 锁的三大类型
锁大致可以分为互斥锁、共享锁、读写锁。

### 1. 互斥锁(排它锁)
互斥锁，即只有一个线程能够访问被互斥锁保护的资源。

在访问共享资源之前，对其进行**加锁**操作。在访问完成之后进行**解锁**操作。加锁后，其他试图加锁的线程会被阻塞，直到当前线程解锁。解锁后，原本等待状态的线程变为就绪状态，重新竞争锁。

### 2. 共享锁
共享锁，即允许多个线程共同访问资源。

### 3. 读写锁
读写锁既是互斥锁，又是共享锁。在读模式下，是共享锁；在写模式下，是互斥锁。
* 读读：共享锁
* 读写：互斥锁
* 写写：互斥锁


## 互斥锁
`synchronized` and `Lock` 都是典型的互斥锁。

### 1. `synchronized` and `Lock` 的区别
* `synchronized` 是 JVM 关键字，而 `Lock` 是 Java 类。
* `synchornized` 不用处理异常状态下的锁释放，当资源使用完毕后或链接断开时，会自动释放锁，而 `Lock` 需要显式调用释放锁。
* `Lock` 接口提供了更多可适配的类和方法，包括非公平锁、读写锁。


---



某一时刻，只能有唯一一个线程去访问`synchronized`修饰的方法。
所有的**静态同步方法**用的是同一把锁--类对象本身(Class)。


## 1. 演示锁的8中情况


## 2. 公平锁和非公平锁
1. 公平锁：加锁前，先查看是否有排队等待的线程，有的话，优先处理排在前面的线程，**先来先得**。阳光普照，效率相对低
2. 非公平锁：线程加锁时，直接尝试获取锁，获取不到就自动到队尾等待。线程饿死，效率高

总结：更多的是直接使用非公平锁：非公平锁比公平锁性能高5～10倍，因为公平锁需要在多核情况下维护一个队列，如果当前线程不是队列的第一个，无法获取锁，增加了线程切换次数。

卖票例子中
```java
ReentrantLock(boolean fair);
   
private final ReentrantLock lock = new ReentrantLock(); // NonfairSync()
```

* https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html


## 3. 可重入锁
* `synchronized`(隐式) and `Lock`(显式) 都是可重入锁


## 4. 死锁
### 1. 什么是死锁？
### 2. 产生死锁的原因？
   1. 系统资源不足
   2. 进程运行推进顺序不合适
   3. 资源分配不当

### 3. 如何验证死锁？
使用`jps + jstack`
1. `jps` (类似 Linux `ps -ef`)
2. `jstack` (JVM 自带的堆栈跟踪工具)
```
$ jps -l
18787 org.jetbrains.jps.cmdline.Launcher
18788 com.ylqi007.chap06.DeadLockDemo
18827 jdk.jcmd/sun.tools.jps.Jps
654 com.intellij.idea.Main

$ jstack 18788
...
...
===================================================
"Thread-A":
        at com.ylqi007.chap06.DeadLockDemo.lambda$main$0(DeadLockDemo.java:21)
        - waiting to lock <0x000000052fa8afa8> (a java.lang.Object)
        - locked <0x000000052fa8af98> (a java.lang.Object)
        at com.ylqi007.chap06.DeadLockDemo$$Lambda$14/0x0000007001001208.run(Unknown Source)
        at java.lang.Thread.run(java.base@17.0.13/Thread.java:840)
"Thread-B":
        at com.ylqi007.chap06.DeadLockDemo.lambda$main$1(DeadLockDemo.java:35)
        - waiting to lock <0x000000052fa8af98> (a java.lang.Object)
        - locked <0x000000052fa8afa8> (a java.lang.Object)
        at com.ylqi007.chap06.DeadLockDemo$$Lambda$15/0x0000007001001428.run(Unknown Source)
        at java.lang.Thread.run(java.base@17.0.13/Thread.java:840)

Found 1 deadlock.
```


## Reference
* [【JUC并发编程06】多线程锁 （公平锁和非公平锁，死锁，可重锁）](https://blog.csdn.net/xt199711/article/details/122770046?spm=1001.2014.3001.5501)