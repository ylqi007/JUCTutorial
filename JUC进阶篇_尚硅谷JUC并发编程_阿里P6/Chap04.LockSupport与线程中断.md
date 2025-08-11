# Chap04. LockSupport 与线程中断

## 1. 线程中断机制
### 1.1 蚂蚁金服面试题
![Thread_interrupts.png](images/Thread_interrupts.png)
* 如何**中断**一个运行中的线程？ 
* 如何**停止**一个运行中的线程？


### 1.2 什么是中断机制
1. 首先，一个线程不应该由其他线程来强制中断或停止，而是应该**由线程自己自行停止**，自己来决定自己的命运，所以，`Thread.stop()`, `Thread.suspend()`, `Thread.resume()`都已经被废弃了。
2. 其次，在Java中没有办法立即停止一条线程，然而停止线程却显得尤为重要，如取消一个耗时操作。因此，Java提供了一种用于停止线程的协商机制--**中断**，也即**中断标识协商机制**
   * **中断只是一种协作协商机制，Java没有给中断增加任何语法，中断的过程完全需要程序员自行实现**。若要中断一个线程，你需要手动调用该线程`interrupt()`方法，该方法也仅仅是将该线程对象的**中断标识**设置为`true`，接着你需要自己写代码不断检测当前线程的标识位，如果为`true`，表示别的线程请求这条线程中断，此时究竟应该做什么需要你自己写代码实现。
   * 每个线程对象都有一个**中断标识位**，用于表示线程是否被中断；该标识位为true表示中断，为false表示未中断；通过调用线程对象的`interrupt()`方法将该线程的标识位设置为true；可以在别的线程中调用，也可以在自己的线程中调用。


### 1.3 中断的相关API的三大方法
* `public void interrupt()`
  * 实例方法 Just to set the interrupt flag
  * 实例方法仅仅是设置**该线程**的中断状态为true，发起一个协商而不会立刻停止线程
* `public static boolean interrupted()`
  * 静态方法`Thread.interrupted()`
  * **判断线程是否被中断**并**清除当前中断状态**（做了两件事情）
    1. 返回当前线程的中断状态，测试当前线程是否已被中断
    2. 将当前线程的中断状态清零并重新设置为false，清除线程的中断状态
  * 这个方法有点不好理解在于如果连续两次调用此方法，则第二次返回false，因为连续调用两次的结果可能不一样
* `public boolean isInterrupted()`
  * 实例方法
  * 判断当前线程是否被中断（通过检查中断标志位）


### 1.4 大厂面试题中断机制考点
#### 1. 如何停止中断运行中的线程？
1. 通过一个 `volatile` 变量实现
2. 通过 `AtomicBoolean`
3. 通过 `Thread` 类自带的中断API实例方法实现----在需要中断的线程中**不断监听中断状态**，一旦发生中断，就执行相应的中断处理业务逻辑stop线程。
* 示例代码: [InterruptDemo1.java](../AdvanceDemo01/src/main/java/com/ylqi007/chap04interrupt/InterruptDemo1.java)


#### 2. 当前线程的中断标识为true，是不是线程就立刻停止？
答案是不立刻停止

具体来说，当对一个线程，调用 `interrupt()` 时：
1. 如果线程处于正常活动状态，那么会将该线程的中断标志设置为`true`，**仅此而已，被设置中断标志的线程将继续正常运行，不受影响**，所以 `interrupt()` 并不能真正的中断线程，需要被调用的线程自己进行配合才行，对于不活动的线程没有任何影响。 
2. 如果线程处于阻塞状态（例如`sleep,wait,join`状态等），在别的线程中调用当前线程对象的 `interrupt()` 方法，那么线程将立即退出被阻塞状态（interrupt状态也将被清除），并抛出一个`InterruptedException` 异常。
   * 源码分析如下
   * ![Thread_interrupt_method.png](images/Thread_interrupt_method.png)
     * `t.inerrupted = true` 时，清除interrupt状体，即将 `t.interrupt` 重置为 `false`，然后返回 `true`。
     * `t.interrupted = false` 时，已经处于 `t.interrupt = false` 状态，直接返回 `false`。
   * 总之，需要记住的是，**中断**只是一种协商机制，修改中断标识位仅此而已，不是立即stop中断。

实例代码: [InterruptDemo3.java](../AdvanceDemo01/src/main/java/com/ylqi007/chap04interrupt/InterruptDemo3.java)


#### 3. 对于静态方法Thread.interrupted()和实例方法isInterrupted()区别在于：
* 静态方法`Thread.interrupted()`，谈谈你的理解？
  * ![Thread_interrupted_method.png](images/Thread_interrupted_method.png)
* 示例代码：[InterruptDemo4.java](../AdvanceDemo01/src/main/java/com/ylqi007/chap04interrupt/InterruptDemo4.java)

对于**静态方法**`Thread.interrupted()`和**实例方法**`isInterrupted()`区别在于：
* 静态方法`Thread.interrupted()`将会清除中断状态（传入的参数ClearInterrupted为`true`）
* 实例方法`isInterrupted()`则不会（传入的参数ClearInterrupted为`false`）
* ![Thread_interrupt_interrupted.png](images/Thread_interrupt_interrupted.png)


### 1.5 总结
1. `public void interrupt()` 是一个**实例方法**，它通知目标线程中断，也仅仅是设置目标线程的中断标志位为**true**
2. `public boolean isInterrupted()` 是一个**实例方法**，它判断当前线程是否被中断（通过检查中断标志位）并获取中断标志
3. `public static boolean interrupted()` 是一个**静态方法**，返回当前线程的中断真实状态（boolean类型）后会将当前线程的中断状态设为`false`，此方法调用之后会清楚当前线程的中断标志位的状态（将中断标志置为false了），返回当前值并清零置为false。



## 2. LockSupport是什么






## 3. 线程等待唤醒机制
### 唤醒方法
1. synchronized -- wait -- notify
   * [Demo01SynchronizedWaitNotify.java](../AdvanceDemo01/src/main/java/com/ylqi007/chap04locksupport/Demo01SynchronizedWaitNotify.java)
2. Lock Condition
   * 线程先要获得并持有锁，必须在锁块中
   * 必须先等待，后唤醒，线程才能够被唤醒
   * [Demo02LockConditionAwaitSignal.java](../AdvanceDemo01/src/main/java/com/ylqi007/chap04locksupport/Demo02LockConditionAwaitSignal.java)
3. LockSupport
   * 许可证最多只有一个，不会累计
   * park() 等到，类似 wait，await
   * unpack() 唤醒，类似 notify，signal
   1. 正常 + 无锁块要求
   2. 之前错误的先唤醒后等待，LockSupport照样支持
   3. 成双成对要牢记


线程阻塞需要消耗凭证(permit)，这个凭证最多只有一个
* 当调用 park() 时
  * 如果有凭证，则会直接消耗这个 permit，然后正常退出
  * 如果无凭证，就必须一直等带 permit 可用
* 当调用 unpakc() 时
  * 它只会增加一个凭证，但凭证最多只能有一个，累加无效


![img.png](img.png)