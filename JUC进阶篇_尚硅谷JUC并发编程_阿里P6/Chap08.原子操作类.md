# Chap08. 原子操作类
Atomic 翻译成中文是原子的意思。在化学上，我们知道原子是构成一般物质的最小单位，在化学反应中是不可分割的。
**在我们这里 Atomic 是指一个操作是不可中断的。即使是在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程干扰。**


## 1. 基础类型原子类
* `AtomicInteger`：整型原子类。  An **int** value that may be updated atomically.
* `AtomicBoolean`：布尔型原子类。A **boolean** value that may be updated atomically.
* `AtomicLong`：长整型原子类。   A **long** value that may be updated atomically.


### 1.1 常用 API 简介
```java
public final int get() //获取当前的值
public final int getAndSet(int newValue)//获取当前的值，并设置新的值
public final int getAndIncrement()      //获取当前的值，并自增
public final int getAndDecrement()      //获取当前的值，并自减
public final int getAndAdd(int delta)   //获取当前的值，并加上预期的值
boolean compareAndSet(int expect, int update)   //如果输入的数值等于预期值，则以原子方式将该值设置为输入值（update）
public final void lazySet(int newValue) //最终设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
```

### 1.2 使用案例
使用 `AtomicInteger` 实现线程安全的 i++ 操作
* 示例代码：[Demo01AtomicInteger.java](../AdvancedJUC/src/main/java/com/ylqi007/chap08atomic/Demo01AtomicInteger.java)


## 2. 数组类型原子类
* `AtomicIntegerArray`：整型数组原子类。 An int array in which elements may be updated atomically.
* `AtomicLongrArray`：长整型数组原子类。 A long array in which elements may be updated atomically.
* `AtomicReferenceArray`：用类型数组原子类。An array of object references in which elements may be updated atomically.


### 2.1 常用 API 简介
```java
public final int get(int i) //获取 index=i 位置元素的值
public final int getAndSet(int i, int newValue)//返回 index=i 位置的当前的值，并将其设置为新值：newValue
public final int getAndIncrement(int i)//获取 index=i 位置元素的值，并让该位置的元素自增
public final int getAndDecrement(int i) //获取 index=i 位置元素的值，并让该位置的元素自减
public final int getAndAdd(int i, int delta) //获取 index=i 位置元素的值，并加上预期的值
boolean compareAndSet(int i, int expect, int update) //如果输入的数值等于预期值，则以原子方式将 index=i 位置的元素值设置为输入值（update）
public final void lazySet(int i, int newValue)//最终 将index=i 位置的元素设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
```


### 1.2 使用案例
代码示例：[Demo02AtomicIntegerArray.java](../AdvancedJUC/src/main/java/com/ylqi007/chap08atomic/Demo02AtomicIntegerArray.java)


## 3. 引用类型原子类
* `AtomicReference`: 引用类型原子类
* `AtomicStampedReference`：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
  * 解决修改过几次
* `AtomicMarkableReference`：原子更新带有标记的引用类型。该类将 boolean 标记与引用关联起来
  * 解决是否修改过，它的定义就是将标记戳简化为 true/false，类似于一次性筷子

示例代码：[Demo03AtomicMarkableReference.java](../AdvancedJUC/src/main/java/com/ylqi007/chap08atomic/Demo03AtomicMarkableReference.java)


## 4. 对象的属性修改原子类
* `AtomicIntegerFieldUpdater`：原子更新对象中int类型字段的值。A reflection-based utility that enables atomic updates to designated volatile int fields of designated classes.
* `AtomicLongFieldUpdater`：原子更新对象中Long类型字段的值
* `AtomicReferenceFieldUpdater`：原子更新对象中引用类型字段的值


### 4.1 使用目的
以一种线程安全的方式操作非线程安全对象的某些字段。


### 4.2 使用要求
* 更新的对象属性必须使用`volatile`修饰符
* 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
  * `public abstract class AtomicIntegerFieldUpdater<T> extends Object`


### 4.3 使用案例
示例代码：
1. [Demo04AtomicIntegerFieldUpdater.java](../AdvancedJUC/src/main/java/com/ylqi007/chap08atomic/Demo04AtomicIntegerFieldUpdater.java)
2. [Demo05AtomicReferenceFieldUpdater.java](../AdvancedJUC/src/main/java/com/ylqi007/chap08atomic/Demo05AtomicReferenceFieldUpdater.java)


## 5. 原子操作增强类原理深度解析


## Reference
* https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/package-summary.html
* https://www.yuque.com/gongxi-wssld/csm31d/alzt6pimnp6fwegp


