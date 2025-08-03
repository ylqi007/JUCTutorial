# Chap03. 线程间的通信
线程间的通信有两种方式
1. 关键字`synchronized`
2. `wait()/notify()`结合使用，实现等待/通知模式。`Lock`接口中的`newCondition()`方法返回的`Condition`对象，`Condition`对象也可以实现等待/通知模式。
   * 使用`notify()`通知时，JVM会随机唤醒某个等待线程。
   * 使用`Condition`类可以进行选择性通知。`Condition`比较常用的两个方法：
     * `await()`会使当前线程等待，同时释放锁，当其他线程调用`signal()`时，线程可以重新获得锁并继续执行。
     * `signal()`用于唤醒一个等待的线程。

## 多线程编程步骤(中部)
1. 创建资源类，在资源类中创建属性和操作方法
2. 在资源类的操作方法中
   1. 判断
   2. 干活
   3. 通知
3. 创建多个线程，调用资源类的操作方法
4. 防止虚假唤醒问题。将判断条件写到 while-loop 中


## 1. `synchronized`实现案例
创建两个线程，对同一个`number`值进行操作，一个线程实现`+1`，一个线程实现`-1`，两个线程交替实现多次。
* `demo01/src/main/java/com/ylqi007/communication/ThreadDemo01.java`


## 2. 虚假唤醒问题
虚假唤醒问题主要在多线程中出现。
`ThreadDemo01.java`中只有2个线程，但是如果增加到4个线程时，就会出现问题。
* `demo01/src/main/java/com/ylqi007/communication/ThreadDemo02.java`
* https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Object.html

总的来说，`wait()`方法使线程在哪里睡就在哪里被唤醒。


## 3. `Lock`实现案例
在`Lock`接口中，有一个`newCondition()`方法，该方法返回一个`Condition`绑定到该实例的Lock实例？

`Condition`类中有`await()`和`signalAll()`等方法，和`synchronized`实现案例中的`await()`和`notifyAll()`方法相同。
所以通过`Lock`接口创建一个`Condition`对象，由该对象的方法进行等待和唤醒操作。


## 4. 线程间的定制化通信
启动三个线程，按照如下要求
1. AA 打印5次，BB 打印10次，CC 打印15次
2. AA 打印5次，BB 打印10次，CC 打印15次
3. 。。。
4. 总共执行 10 次


## Reference
* [【JUC并发编程03】线程间通信](https://blog.csdn.net/xt199711/article/details/122722896?spm=1001.2014.3001.5501)