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
* `lock()` 是最基础的获取锁的方法。在线程获取锁时如果锁已被其他线程获取，则进行等待，是最初级的获取锁的方法。
  * `lock()` 方法不能被中断，这会带来很大的隐患：一旦陷入死锁，`lock()` 就会陷入永久等待，所以一般我们用 `tryLock()` 等其他更高级的方法来代替 `lock()`，下面我们就看一看 `tryLock()` 方法。
* `tryLock()` 用来尝试获取锁，如果当前锁没有被其他线程占用，则获取成功，返回 `true`，否则返回 `false`，代表获取锁失败。相比于 `lock()`，这样的方法显然功能更强大，我们可以根据是否能获取到锁来决定后续程序的行为。
  * 因为该方法会立即返回，即便在拿不到锁时也不会一直等待，所以通常情况下，我们用 if 语句判断 tryLock() 的返回结果，根据是否获取到锁来执行不同的业务逻辑
* `tryLock()` 的重载方法是 `tryLock(long time, TimeUnit unit)`，这个方法和 `tryLock()` 很类似，区别在于 `tryLock(long time, TimeUnit unit)` 方法会有一个超时时间，在拿不到锁时会等待一定的时间，如果在时间期限结束后，还获取不到锁，就会返回 `false`；如果一开始就获取锁或者等待期间内获取到锁，则返回 `true`。
* `lockInterruptibly()`: 这个方法的作用就是去获取锁，如果这个锁当前是可以获得的，那么这个方法会立刻返回，但是如果这个锁当前是不能获得的（被其他线程持有），那么当前线程便会开始等待，除非它等到了这把锁或者是在等待的过程中被中断了，否则这个线程便会一直在这里执行这行代码。一句话总结就是，除非当前线程在获取锁期间被中断，否则便会一直尝试获取直到获取到为止。
  * 顾名思义，`lockInterruptibly()` 是可以响应中断的。相比于不能响应中断的 synchronized 锁，`lockInterruptibly()` 可以让程序更灵活，可以在获取锁的同时，保持对中断的响应。我们可以把这个方法理解为超时时间是无穷长的 `tryLock(long time, TimeUnit unit)`，因为 `tryLock(long time, TimeUnit unit)` 和 `lockInterruptibly()` 都能响应中断，只不过 `lockInterruptibly()` 永远不会超时。
* `unlock()`: 最后要介绍的方法是 unlock() 方法，是用于解锁的，u方法比较简单，对于 ReentrantLock 而言，执行 unlock() 的时候，内部会把锁的“被持有计数器”减 1，直到减到 0 就代表当前这把锁已经完全释放了，如果减 1 后计数器不为 0，说明这把锁之前被“重入”了，那么锁并没有真正释放，仅仅是减少了持有的次数。



`Lock`实现提供比使用`synchronized`方法更广泛的锁定操作。它们允许更灵活的结构化，可能具有完全不同的属性，并且可以支持多个相关联的`Condition`对象。

当在不同范围内发生锁定和解锁时，必须注意确保在锁定时执行所有的所有代码由`try-finally`或`try-catch`保护，以确保在必要时释放锁定。

`Lock`实现提供了使用`synchronized`方法和语句的附加功能，通过提供非阻塞来尝试获取锁`tryLock()`，尝试获取可被中断的锁`lockInterruptibly()`，以及尝试获取可以超时`tryLock(long, TimeUnit)`。

### 2. 常用实现类
1. `ReentrantLock`(可重入锁)
   * 最常用，功能最全，支持公平锁（构造时传 `true`）
   * 支持多 `Condition`，替代 `wait/notify`
2. `ReentrantReadWriteLock`(读写锁)
   * 提供**读锁(共享)** + **写锁(独占)**
   * 适合**读多写少**场景
3. `StampedLock`
   * JDK8 引入，支持**乐观读**，性能优于 ReentrantReadWriteLock
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


## 3. 使用多线程模拟实现卖票案例
* 3 个售票员需要卖出 30 张票。
* 每个线程代表一个售票员 。

### 1. 使用 synchronized 实现
* [SellTickets.java](../demo01/src/main/java/com/ylqi007/chap02sync/SellTickets.java)

### 2. 使用 Lock 实现卖票案例
* [LSellTickets.java](../demo01/src/main/java/com/ylqi007/chap02lock/LSellTickets.java)


## 4. synchronized和Lock两者区别
1. synchronized是Java内置的关键字，而Lock不是内置，是一个类，可以实现同步访问且比synchronized中的方法更加丰富。
2. synchronized不会手动释放锁，而Lock需要手动释放锁(不解锁会出现死锁，需要在finally块中释放锁)。
3. lock等待锁的线程会相应中断，而synchronized不会中断，只会一直等待。
4. 通过Lock可以知道有没有成功获取锁，而synchronized不会，只会一直等待。
5. Lock可以提高多个线程进行读操作的效率(当多个线程竞争的时候，锁会出现死锁，需要在finally块中释放锁)。
6. Lock等待锁的线程会相应中断，而synchronized不会中断，只会一直等待。


## 多线程编程步骤(上部)
1. 创建资源类，在资源类创建属性和操作方法。
   * 高内聚，低耦合的思想：将资源的属性和操作属性的方法放在资源类中，避免调用其他类的方法。
2. 创建多个线程，调用资源类的操作方法。

### 创建线程的多种方式
1. 继承Thread类 （Java是单继承，继承很珍贵，很少用）
2. 实现Runnable接口
3. 使用Callable接口
4. 使用线程池


## Reference
1. 可重入锁: `java.util.concurrent.locks.ReentrantLock`
2. [【JUC并发编程02】Lock接口](https://blog.csdn.net/xt199711/article/details/122720198?spm=1001.2014.3001.5501)
3. [java.util.concurrent.locks](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/package-summary.html)
4. [【多线程】锁机制详解](https://blog.csdn.net/qq_34416331/article/details/107764522)
5. [21 如何看到 synchronized 背后的“monitor 锁”？](https://learn.lianglianglee.com/%e4%b8%93%e6%a0%8f/Java%20%e5%b9%b6%e5%8f%91%e7%bc%96%e7%a8%8b%2078%20%e8%ae%b2-%e5%ae%8c/21%20%e5%a6%82%e4%bd%95%e7%9c%8b%e5%88%b0%20synchronized%20%e8%83%8c%e5%90%8e%e7%9a%84%e2%80%9cmonitor%20%e9%94%81%e2%80%9d%ef%bc%9f.md)
6. [22 synchronized 和 Lock 孰优孰劣，如何选择？](https://learn.lianglianglee.com/%e4%b8%93%e6%a0%8f/Java%20%e5%b9%b6%e5%8f%91%e7%bc%96%e7%a8%8b%2078%20%e8%ae%b2-%e5%ae%8c/22%20synchronized%20%e5%92%8c%20Lock%20%e5%ad%b0%e4%bc%98%e5%ad%b0%e5%8a%a3%ef%bc%8c%e5%a6%82%e4%bd%95%e9%80%89%e6%8b%a9%ef%bc%9f.md)
7. [23 Lock 有哪几个常用方法？分别有什么用？](https://learn.lianglianglee.com/%e4%b8%93%e6%a0%8f/Java%20%e5%b9%b6%e5%8f%91%e7%bc%96%e7%a8%8b%2078%20%e8%ae%b2-%e5%ae%8c/23%20Lock%20%e6%9c%89%e5%93%aa%e5%87%a0%e4%b8%aa%e5%b8%b8%e7%94%a8%e6%96%b9%e6%b3%95%ef%bc%9f%e5%88%86%e5%88%ab%e6%9c%89%e4%bb%80%e4%b9%88%e7%94%a8%ef%bc%9f.md)