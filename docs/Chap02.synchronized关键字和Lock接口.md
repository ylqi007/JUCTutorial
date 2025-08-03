# Chap02. synchronized关键字 & Lock接口

## 1. 复习 synchronized
### 1. `synchronized` 的意义
核心作用：保证多线程环境下
1. 互斥：同一时刻只有一个线程能执行同步代码块/方法
2. 可见性：一个线程对共享变量的修个对其他线程立即可见(内存语义类似 `volatile` + 锁)

它是 Java 内置的监视器锁(Monitor Lock)，基于**对象监视器**实现。


### 2. 用法
#### 2.1 修饰实例方法
锁的是**当前对象(this)**:
```java
public synchronized void doSomething() {
    // 同一对象上，多个线程不能同时执行
}
```

#### 2.2 修饰静态方法
锁的是**类对象(Class)**:
```java
public static synchronized void doStatic() {
    // 锁的是类的 Class 对象
}
```

#### 2.3 同步代码块
锁的是**指定对象**:
```java
public void doWork() {
    synchronized (lock) {
        // 只有一个线程可以进入
    }
}
```

> 推荐：用`private final`对象作为锁（避免锁泄露）：
```java
private final Object lock = new Object();
```


### 3. 底层原理（简要）不懂
* 编译后生成 monitorenter/monitorexit 字节码
* JVM 通过 对象头（Mark Word） 记录锁状态
* 可能涉及 偏向锁 → 轻量级锁 → 重量级锁 升级


### 4. Best Practice (实际开发建议)
1. 锁粒度尽量小
   * 不要整个方法都 `synchronized`，而**只锁必要的共享资源访问部分**
   * 避免扩大锁范围导致性能下降
2. 使用私有锁对象
   * 不直接锁 `this` 或 `Class`，避免外部代码意外持有锁：
   ```java
   private final Object lock = new Object();
   synchronized (lock) { ... }
   ```
3. 避免死锁
   * 遵循一致的加锁顺序
   * 必要时用 `tryLock()`（`ReentrantLock` 提供）
4. 不要同时混用多种锁（如 `synchronized` + `Lock`）
   * 可能导致难以维护的死锁和不可预测行为
5. 性能要求高时
   * 用 `java.util.concurrent` 包的替代方案（如 `ReentrantLock`、`ReadWriteLock`、`StampedLock`）

### 5. 适用场景
* 简单同步需求（计数器、共享变量保护）
* 线程安全的懒加载（单例双重检查）
* 保护不可并发修改的集合/对象

> ✅一句话总结: `synchronized` = 简单好用的内置锁，保证原子性 + 可见性，但要 控制锁粒度，避免锁泄露，必要时用更灵活的 Lock 替代。


## 2. Lock 接口
### 1. Lock 接口的意义
* 提供比 `synchronized` 更灵活的锁机制，可手动获取和释放。
* 支持**可中断、可超时、公平锁、多条件变量**等高级特性。

**核心方法:**
```java
public interface Lock {
   void lock();                     // 阻塞获取锁
   void lockInterruptibly();        // 可中断获取锁
   boolean tryLock();               // 尝试获取锁（立即返回）
   boolean tryLock(long time, TimeUnit unit); // 超时获取锁
   void unlock();                   // 释放锁
   Condition newCondition();        // 获取 Condition 对象
}
```

### 2. 常用实现类
1. `ReentrantLock`()
   * 最常用，功能最全，支持公平锁（构造时传 `true`）
   * 支持多 `Condition`，替代 `wait/notify`
2. `ReentrantReadWriteLock`()
   * 提供**读锁(共享)** + **写锁(独占)**
   * 适合**读多写少**场景
3. `StampedLock`()
   * 支持**乐观读**，性能更好
   * 非可重入锁

### 3. ReentrantLock (可重入锁)
`ReentrantLock`(可重入锁)是一种递归、无阻塞的同步机制，也叫做递归锁，指的是同一线程在外层函数获得锁之后，内层递归函数仍然可以有获取该锁的代码，但不受影响。
`ReentrantLock` and `synchronized` 都是可重入锁。
* Demo: [LSellTickets.java](../demo01/src/main/java/com/ylqi007/chap02lock/LSellTickets.java)

### 4. 基本用法
#### 1. 标准写法(避免死锁)
```java
private final Lock lock = new ReentrantLock();

public void doWork() {
    lock.lock();
    try {
        // 临界区
    } finally {
        lock.unlock(); // 必须在 finally 中释放
    }
}
```

#### 2. `tryLock`(避免长时间等待)
```java
private final Lock lock = new ReentrantLock();

public void doWork() {
   if (lock.tryLock(1, TimeUnit.SECONDS)) {
      try {
         // 成功获取锁
      } finally {
         lock.unlock();
      }
   } else {
      // 获取失败的逻辑
   }
}
```

#### 3. `Condition`(替代 `wait/notify`)
```java
private final Lock lock = new ReentrantLock();
Condition notEmpty = lock.newCondition();

public void doWork() {
   lock.lock();
   try {
      while (queue.isEmpty()) {
         notEmpty.await(); // 类似 Object.wait()
      }
      // 处理元素
      notEmpty.signal(); // 唤醒等待线程
   } finally {
      lock.unlock();
   }
}
```

### 5. Best Practices
1. 始终配合 `try/finally`
   * 确保锁一定会释放，避免死锁。
2. 锁粒度最小化
   * 尽量缩小加锁范围，减少性能损耗。
3. 优先 `tryLock`
   * 用 `tryLock()` 避免死锁，适合高并发场景。
4. 优先 `ReentrantLock`
   * 一般场景下用 `ReentrantLock` 替代 `synchronized`，尤其是需要**超时/可中断**的。
5. `ReentrantReadWriteLock` and `StampledLock`
   * **读多写少** → `ReentrantReadWriteLock`; 
   * **乐观读场景** → `StampedLock`。
6. 避免混用多种锁
   * 比如同一对象既用 `synchronized` 又用 `Lock`，容易死锁。

### 6. 适用场景
* 需要超时/中断的加锁
* 需要多个条件变量（比 wait/notify 更灵活）
* 复杂并发控制（如生产者-消费者队列）
* 高并发读写分离（读写锁、StampedLock）


## Reference
1. 可重入锁: `java.util.concurrent.locks.ReentrantLock`
2. [【JUC并发编程02】Lock接口](https://blog.csdn.net/xt199711/article/details/122720198?spm=1001.2014.3001.5501)
3. [java.util.concurrent.locks](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/package-summary.html)
4. [【多线程】锁机制详解](https://blog.csdn.net/qq_34416331/article/details/107764522)


# --- Split ---
---

### 1.1 synchronized 的作用范围
`synchronized` 是Java中的关键字，是一种同步锁，能够修饰一个代码块，方法，静态方法，类，来控制访问顺序。
```java
synchronized(this) {
    xxx
}
```
它修饰的对象有以下几种：
1. 修饰一个代码块
2. 修饰一个方法
3. 修饰一个静态方法
4. 修饰一个类

### 2. synchronized 实现卖票实例
3 个售票员需要卖出 30 张票。
每个线程代表一个售票员。

### 多线程编程步骤(上部)
1. 创建资源类，在资源类创建属性和操作方法。
   * 高内聚，低耦合的思想：将资源的属性和操作属性的方法放在资源类中，避免调用其他类的方法。
2. 创建多个线程，调用资源类的操作方法。


## 2. 什么是 Lock 接口
`Lock`实现提供比使用`synchronized`方法更广泛的锁定操作。它们允许更灵活的结构化，可能具有完全不同的属性，并且可以支持多个相关联的`Condition`对象。

当在不同范围内发生锁定和解锁时，必须注意确保在锁定时执行所有的所有代码由`try-finally`或`try-catch`保护，以确保在必要时释放锁定。

`Lock`实现提供了使用`synchronized`方法和语句的附加功能，通过提供非阻塞来尝试获取锁`tryLock()`，尝试获取可被中断的锁`lockInterruptibly()`，以及尝试获取可以超时`tryLock(long, TimeUnit)`。


## 3. 创建线程的多种方式
1. 继承Thread类 （Java是单继承，继承很珍贵，很少用）
2. 实现Runnable接口
3. 使用Callable接口
4. 使用线程池


## 4. 使用 Lock 实现卖票的例子
3个售票员，卖出30张票。
* `demo01/src/main/java/com/ylqi007/lock/LSellTickets.java`


## 5. synchronized和Lock两者区别
1. synchronized是Java内置的关键字，而Lock不是内置，是一个类，可以实现同步访问且比synchronized中的方法更加丰富。
2. synchronized不会手动释放锁，而Lock需要手动释放锁(不解锁会出现死锁，需要在finally块中释放锁)。
3. lock等待锁的线程会相应中断，而synchronized不会中断，只会一直等待。
4. 通过Lock可以知道有没有成功获取锁，而synchronized不会，只会一直等待。
5. Lock可以提高多个线程进行读操作的效率(当多个线程竞争的时候，锁会出现死锁，需要在finally块中释放锁)。
6. Lock等待锁的线程会相应中断，而synchronized不会中断，只会一直等待。
