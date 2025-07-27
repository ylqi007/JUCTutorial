# Chap02. Lock接口

## 1. 复习 synchronized
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


## Reference
1. 可重入锁: `java.util.concurrent.locks.ReentrantLock`
2. [【JUC并发编程02】Lock接口](https://blog.csdn.net/xt199711/article/details/122720198?spm=1001.2014.3001.5501)