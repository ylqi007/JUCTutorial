

## synchronized
1. 创建资源类，定义属性和方法
2. 创建线程类，重写run方法
3. 创建线程对象，启动线程
4. 在run方法中加入同步代码块
5. 在run方法中加入同步方法

## Lock
1. 可重入锁: `java.util.concurrent.locks.ReentrantLock`