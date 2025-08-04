# 1. 什么是 JUC

## 1.1 JUC 简介
JUC 是 `java.util.concurrent` 工具包的简称。这是一个处理线程的工具包，从 JDK 1.5 开始出现。

## 1.2 进程与线程
进程 (Process) 是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。
在当代面向线程设计的计算机结构中，**进程是线程的容器**。
**程序**是指令、数据及其组织形式的描述，**进程**是程序的实体。

> 进程(Process): 进程是程序的一次执行，是系统进行资源分配和调度的基本单位。每个进程都有独立的内存空间和系统资源，例如代码、数据、打开的文件等。
> 线程(thread): 线程是进程中的一个执行单元，是CPU调度的最小单位。一个进程可以包含多个线程，这些线程共享进程的资源，但每个线程都有自己的程序计数器、栈等。

线程 (thread) 是操作系统能够进行运算调度的最小单位。它被包含在进程之中，是进程中的实际运作单位。
一条线程指的是进程中一个单一顺序的控制流，一个进程中可以并发多个线程，每条线程并行执行不同的任务。

**总结来说:**
* 进程：指在系统中正在运行的一个应用程序；程序一旦运行就是进程；进程 —— 资源分配的最小单位。
* 线程：系统分配处理器时间资源的基本单元，或者说进程之内独立执行的一个单元执行流。线程——程序执行的最小单位。

## 1.3 线程的状态
### 1.3.1 线程状态
* [`java.lang.Thread.State`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.State.html)

A thread can be in one of the following states:
* `NEW`           A thread that has not yet started is in this state.
* `RUNNABLE`      A thread executing in the Java virtual machine is in this state.
* `BLOCKED`       A thread that is blocked waiting for a monitor lock is in this state.
* `WAITING`       A thread that is waiting indefinitely for another thread to perform a particular action is in this state. (不见不散)
* `TIMED_WAITING` A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state. (过时不候)
* `TERMINATED`    A thread that has exited is in this state.

A thread can be in only one state at a given point in time. These states are virtual machine states which do not reflect any operating system thread states.

### 1.3.2 wait/sleep 的区别
1. `sleep()` 是 Thread 的静态方法；`wait()` 是 Object 的方法，任何对象实例都能调用。
2. `sleep()` 不会释放锁，也不需要占用锁。`wait()` 会释放锁，但调用它的前提是当前线程占有锁(即代码要在`synchronized`中)。 
3. 都可以被 interrupted 打断。在哪里睡，就在哪里醒。


## 1.4 并发与并行
### 1.4.1 串行模式
串行表示所有任务都一一按先后顺序进行。串行是一次只能取得一个任务，并执行这个任务。

### 1.4.2 并行模式
并行的效率从代码层次上强依赖于多进程/多线程代码，从硬件角度上则依赖于多核 CPU。

### 1.4.3 并发 (concurrent)
~~并发(concurrent)指的是多个程序可以同时运行的现象，更细化的是多进程可以同时运行或者多指令可以同时运行。~~
并发(concurrent)是同一时刻，多个线程在访问同一个资源，多个线程对一个点。
* 春运抢票(多人抢同一个资源，即票)
* 电商秒杀(多人抢多一个资源，商品)


### 1.4.4 总结
* 串行：一次只能执行一个
* 并发：同一时刻，**多个线程**访问同一资源
* 并行：多项工作一起执行，之后再汇总

## 1.5 管程 (Monitor, 监视器，锁)
是一种同步机制，保证同一时间，只有一个线程能访问被保护的数据or代码。

* 管程 (monitor) 是保证了同一时刻只有一个进程在管程内活动,即管程内定义的操作在同一时刻只被一个进程调用(由编译器实现)，但是这样并不能保证进程以设计的顺序执行
* JVM 中同步是基于进入和退出管程 (monitor) 对象实现的，每个对象都会有一个管程 (monitor) 对象，管程 (monitor) 会随着 java 对象一同创建和销毁
* 执行线程首先要持有管程对象，然后才能执行方法，当方法完成之后会释放管程，方法在执行时候会持有管程，其他线程无法再获取同一个管程


## 1.6 用户线程，守护线程
* 用户线程：平时用到的普通线程,自定义线程
* 守护线程：运行在后台,是一种特殊的线程,比如垃圾回收

* 当主线程结束后,用户线程还在运行,JVM 存活
* 当主线程结束后,用户线程还在运行,JVM 存活


## Reference
* [Java中的进程与线程（总结篇）](https://www.cnblogs.com/WuXuanKun/p/6259965.html)
