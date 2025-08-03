# Runnable vs Callable

"`Runnable`是用于提供多线程任务支持的核心接口，`Callable`是在Java 1.5中添加的`Runnable`的改进版本。"

## `Runnable` 接口
```java
package java.lang;

/**
 * The {@code Runnable} interface should be implemented by any
 * class whose instances are intended to be executed by a thread. The
 * class must define a method of no arguments called {@code run}.
 * <p>
 * This interface is designed to provide a common protocol for objects that
 * wish to execute code while they are active. For example,
 * {@code Runnable} is implemented by class {@code Thread}.
 * Being active simply means that a thread has been started and has not
 * yet been stopped.
 * <p>
 * In addition, {@code Runnable} provides the means for a class to be
 * active while not subclassing {@code Thread}. A class that implements
 * {@code Runnable} can run without subclassing {@code Thread}
 * by instantiating a {@code Thread} instance and passing itself in
 * as the target.  In most cases, the {@code Runnable} interface should
 * be used if you are only planning to override the {@code run()}
 * method and no other {@code Thread} methods.
 * This is important because classes should not be subclassed
 * unless the programmer intends on modifying or enhancing the fundamental
 * behavior of the class.
 *
 * @author  Arthur van Hoff
 * @see     java.lang.Thread
 * @see     java.util.concurrent.Callable
 * @since   1.0
 */
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
`Runnable`接口是一个函数式接口，它只有一个`run()`方法，不接受任何参数，也不返回任何值。由于方法签名没有指定throws子句，因此无法进一步传播已检查的异常。它适用于我们不使用线程执行结果的情况，例如，异步打印日志:
```java
@Override
public void run() {
    log.info("你好，{}", "Xxx");
}
```


## `Callable` 接口
```java
package java.util.concurrent;

/**
 * A task that returns a result and may throw an exception.
 * Implementors define a single method with no arguments called
 * {@code call}.
 *
 * <p>The {@code Callable} interface is similar to {@link
 * java.lang.Runnable}, in that both are designed for classes whose
 * instances are potentially executed by another thread.  A
 * {@code Runnable}, however, does not return a result and cannot
 * throw a checked exception.
 *
 * <p>The {@link Executors} class contains utility methods to
 * convert from other common forms to {@code Callable} classes.
 *
 * @see Executor
 * @since 1.5
 * @author Doug Lea
 * @param <V> the result type of method {@code call}
 */
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
`Callable`接口也是一个函数式接口，它只有一个`call()`方法，不接受任何参数，返回一个泛型值V，在方法签名上包含`throws Exception`子句，因此我们可以很容易地进一步传播已检查异常。它适用于我们使用线程执行结果的情况，例如，异步计算阶乘。


## 总结
`Runnable`和`Callable`的不同：
* 有无返回值：`Runnable`的任务不能返回值；`Callable`的任务执行后可返回值。
* 执行机制：`Runnable`可以通过`Thread`和`ExecutorService`启动；`Callable`只可以通过`ExecutorService`启动。
* 异常处理：`Runnable`的`run()`方法不抛出异常，需要内部处理；`Callable`的`call()`方法可以传播已检查异常。


## Reference
* [老徐和阿珍的故事：Runnable和Callable有什么不同？](https://www.cnblogs.com/heihaozi/p/16049860.html)
* [三分钟掌握 Runnable 和 Callable 异同](https://www.cnblogs.com/bestjosephine/p/18756690)