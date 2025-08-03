# Chap07. Callable 接口

在 Java 中，Runnable 和 Callable 接口都用于描述可以在多线程环境中执行的任务，但它们之间有一些重要差别。

## 1. Runnable vs Callable 接口
1. 返回值类型：`Runnable.run()` 方法没有返回值，而 `Callable.call()` 方法有一个泛型返回值。
2. 异常处理：`Runnable.run()` 方法不能抛出受检异常，而 `Callable.call()` 方法可以抛出受检异常。
3. 使用方式：`Runnable` 通常与 `Thread` 一起使用，`Callable` 通常与 `ExecutorService` 和 `Future` 一起使用。




## 创建线程的多种方式
1. 继承 Thread 类
2. 实现 Runnable 接口
3. Callable 接口：有返回值 + FutureTask
4. 线程池

虽然通过实现 `Runnable` 接口可以创建线程，但是 `Runnable` 接口的 `run()` 方法并没有返回值。因此，当线程终止时，我们无法使线程返回结构。为了支持此功能，Java 提供了 `Callable` 接口。
```java
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
```
用途：
* 适用于执行无返回结果的任务（比如打印日志，更新状态）
* 经典用法
    ```java
    new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Running...");
        }
    }).start();
  
    // Java 8+，可以用 Lambda
    new Thread(() -> System.out.println("Running")).start();
    ```

```java
@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```
用途：
* 适用于需要返回结果的任务（如计算、查询）
* 需要与 ExecutorService + Future 配合：
    ```java
    ExecutorService pool = Executors.newFixedThreadPool(2);
    Future<Integer> future = pool.submit(() -> 1 + 2);
    System.out.println(future.get()); // 3
    pool.shutdown();
    ```

### Runnable vs Callable
* Callable 接口有返回值，call(); Runnable 接口无返回值，run()
* Callable 有异常抛出，Runnable 无异常抛出

总结：`Runnable.run()`没有返回值，不抛异常；`Callable.call()`有返回值，抛异常。

因为 Thread 的构造器不接受 Callable 接口的实现，因此无法通过直接传入 Callable 创建 Thread。
需要通过 FutureTask 创建 Thread

* FutureTask 实现了 Runnable 接口
* FutureTask 构造器可以传递 Callable 对象


## Reference
* [【JUC并发编程07】Callable接口](https://blog.csdn.net/xt199711/article/details/122770053?spm=1001.2014.3001.5501)
* [runnable和callable有什么区别?](https://www.itheima.com/news/20240304/111305.html)