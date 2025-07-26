package com.ylqi007;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTaskDemo1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
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

        // System.out.println(futureTask.get());   // 放在此处会导致阻塞：不见不散，一定要等到结果。因此要放在最后

        System.out.println(Thread.currentThread().getName() + " is working on other tasks");

        // System.out.println(futureTask.get());   // 要放在末尾
        // System.out.println(futureTask.get(3, TimeUnit.SECONDS));

        while(true) {
            if(futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread is running, 不要再催了");
            }
        }
    }
}
