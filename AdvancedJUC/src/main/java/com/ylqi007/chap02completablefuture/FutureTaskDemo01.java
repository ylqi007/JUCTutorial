package com.ylqi007.chap02completablefuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 通过 new FutureTask(Callable) 创建一个 FutureTask
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());

        // 开启一个异步线程
        Thread thread = new Thread(futureTask, "Thread-MyCallable");
        thread.start();

        System.out.println(futureTask.get());   // MyCallable的返回值，即为FutureTask的返回值
    }
}

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " is running");
        return "Hello MyCallable";
    }
}