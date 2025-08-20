package com.ylqi007.chap02completablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTaskDemo02 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testGet();

        testIsDone();
    }

    /**
     * FutureTask.get() 会造成阻塞
     *
     * Thread-0 is running
     * Task is over
     * main is working on other tasks ==> main线程在子线程 Thread-0 结束后才继续执行，说明被 main 线程被 blocked 了
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void testGet() throws ExecutionException, InterruptedException {
        // Step 1: Create FutureTask
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            TimeUnit.SECONDS.sleep(5);
            return "Task is over";
        });

        // Step 2: Use FutureTask to create Thread
        Thread thread = new Thread(futureTask);
        thread.start();

        System.out.println(futureTask.get());   // 放在此处会导致阻塞：不见不散，一定要等到结果。因此要放在最后

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        // System.out.println(futureTask.get());   // 要放在末尾
        // System.out.println(futureTask.get(3, TimeUnit.SECONDS));
    }

    /**
     * isDone() 轮询，浪费 CPU 资源
     */
    private static void testIsDone() throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task is over";
        });

        Thread t1 = new Thread(futureTask);
        t1.start();

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        while(true) {
            if(futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("Thread is running, 不要再催了");
            }
        }
    }
}
