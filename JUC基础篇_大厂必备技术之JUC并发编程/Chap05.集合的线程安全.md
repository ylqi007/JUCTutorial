# Chap05. 集合的线程安全

## 1. 集合线程不安全演示
在多线程中使用集合时，为什么会出现线程不安全？

以`ArrayList`为例，它的`add()`方法并没有`synchronized`关键字修饰，源码如下
```java
    // 没有synchronized关键字，线程不安全
    public boolean add(E e) {
        modCount++;
        add(e, elementData, size);
        return true;
    }
```
显然，`add()`方法并没有同步互斥，因此在多线程时，会出现线程异常。
* 测试代码：`demo01/src/main/java/com/ylqi007/chap05/ThreadDemo01ArrayList.java`

解决该异常主要有三种方法：
1. 使用 Vector
2. 使用 Collections
3. 使用 CopyOnWriteArrayList (常用)


### 1. 解决方案1 -- Vector (比较古老的方案，不常用)
以下是`Vector.add()`的源码实现，可见有`synchronized`关键字修饰，它是线程安全的。
```java
    // 有 synchronized，线程安全
    public synchronized void addElement(E obj) {
        modCount++;
        add(obj, elementData, elementCount);
    }
```
因为每次添加元素时都要上锁，而且使用的是重量级锁`synchronized`，十分占用资源，因此效率低下，所以`Vector`使用的并不多。
* Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo02Vector.java`


### 2. 解决方案2 -- Collections.synchronizedList() (也比较古老，不常用)
`Collections.synchronizedList(List list)`方法的源码实现如下，`synchronizedList(List)`方法返回线程安全的 List。
```java
    public static <T> List<T> synchronizedList(List<T> list) {
        return (list instanceof RandomAccess ?
                new SynchronizedRandomAccessList<>(list) :
                new SynchronizedList<>(list));
    }
```
它的使用如下
```java
List<String> list = Collections.synchronizedList(new ArrayList<>());
```
* Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo03Collections.java`


### 3. 解决方案--CopyOnWriteArrayList (写时复制技术，常用)
`CopyOnWriteArrayList`底层采用**写时复制技术**
* 读的时候并发(多线程操作)
* 写的时候独立，先复制数据到某个新区域，将新数据写到新区域中，然后覆盖旧数据，并且开始读新数据。

`CopyOnWriteArrayList.add()`的源码实现
```java
    public boolean add(E e) {
        synchronized (lock) {
            Object[] es = getArray();
            int len = es.length;
            es = Arrays.copyOf(es, len + 1);
            es[len] = e;
            setArray(es);
            return true;
        }
    }
```
使用方法与`ArrayList<>()`一致
```java
List<String> list = new CopyOnWriteArrayList<>();
```
Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo04CopyOnWriteArrayList.java`

### 对以上三种方式的总结
`Vector`和`Collections`虽然可以实现同步，但是都是比较古老，效率低下的方式，所以对`ArrayList`的同步主要采用`CopyOnWriteArrayList`。


## 2. HashSet的线程不安全演示
`HashSet`同时读写也会出现`java.util.ConcurrentModificationException`。
* Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo05HashSet.java`

它的解决方法与`CopyOnWriteArrayList`类似，是通过`CopyOnWriteArraySet`实现线程同步的。
* Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo06CopyOnWriteArraySet.java`


## 3. HashMap的线程不安全演示
`HashMap`同时读写也会出现`java.util.ConcurrentModificationException`。

`ConcurrentHashMap`类解决了`HashMap`的同步问题
* Example: `demo01/src/main/java/com/ylqi007/chap05/ThreadDemo07HashMap.java`


## Reference
* [【JUC并发编程05】集合的线程安全](https://blog.csdn.net/xt199711/article/details/122760641?spm=1001.2014.3001.5501)