package com.atguigu;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());

        Thread t1 = new Thread(futureTask);
        t1.start();

        System.out.println(futureTask.get());
    }
}

/*
Runnable vs Callable
1. 是否有返回
2. 是否抛出异常
 */
class MyThread implements Runnable {
    @Override
    public void run() {
        System.out.println("MyThread is running");
    }
}

class MyCallable implements Callable {
    @Override
    public Object call() throws Exception {
        System.out.println("Calling MyCallable.call()...");
        return "Hello world!";
    }
}