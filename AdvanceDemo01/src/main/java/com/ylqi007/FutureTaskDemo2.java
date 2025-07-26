package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());

        Thread t1 = new Thread(futureTask);
        t1.start();

        System.out.println(futureTask.get());
    }

    /**
     * FutureTask(Callable<V> callable) : Creates a FutureTask that will, upon running, execute the given Callable.
     */
    @Test
    public void testFutureTaskWithCallable1() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask).start();
        System.out.println(futureTask.get());   // 如何获取异步线程的返回结果呢？ futureTask.get()
    }

    @Test
    public void testFutureTaskWithCallable2() throws ExecutionException, InterruptedException {
        // Define a Callable that computes a result
        Callable<Integer> task = () -> {
            System.out.println(Thread.currentThread().getName() + " : Computing the sum of first 10 integers...");
            int sum = 0;
            for(int i=1; i<=10; i++) {
                sum += i;
            }
            return sum;
        };

        // Wrap the Callable with a FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(task);

        // Start the task in a separate thread
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Integer result = futureTask.get();
            System.out.println("Result = " + result);
        } catch (Exception e) {
            System.out.println("Exception = " + e.getMessage());
        }
    }

    @Test
    public void testFutureTaskWithCallable3() throws ExecutionException, InterruptedException {
        // Define the first Callable task
        Callable<Integer> task1 = () -> {
            System.out.println(Thread.currentThread().getName() + " : Task 1: Computing factorial of 5...");
            int factorial = 1;
            for (int i = 1; i <= 5; i++) {
                factorial *= i;
            }
            return factorial;
        };

        // Define the second Callable task that depends on the result of task1
        Callable<Integer> task2 = () -> {
            System.out.println(Thread.currentThread().getName() + " : Task 2: Computing square of factorial...");
            FutureTask<Integer> futureTask1 = new FutureTask<>(task1);
            Thread thread1 = new Thread(futureTask1);
            thread1.start();

            int factorial = futureTask1.get(); // Get the result from task1
            return factorial * factorial; // Return the square of the factorial
        };

        // Wrap the second Callable in a FutureTask
        FutureTask<Integer> futureTask2 = new FutureTask<>(task2);
        Thread thread2 = new Thread(futureTask2);
        thread2.start();

        try {
            // Get the result of task2
            Integer result = futureTask2.get();
            System.out.println("Final Result: " + result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    /**
     * public FutureTask(Runnable runnable, V result)
     * Creates a FutureTask that will, upon running, execute the given Runnable, and arrange that get will return the given result on successful completion.
     * Using FutureTask with Runnable and a predefined result
     */
    @Test
    public void testFutureTaskWithRunnable1() throws ExecutionException, InterruptedException {
        // Predefined result for the FutureTask
        String result = "Task Result";
        FutureTask<String> futureTask = new FutureTask<>(new MyRunnable(), result);

        new Thread(futureTask).start();

        System.out.println(futureTask.get());
    }

    @Test
    public void testFutureTaskWithRunnable2() throws ExecutionException, InterruptedException {
        // Runnable task with side effects
        Runnable task = () -> {
            System.out.println("Performing a side effect...");
            try {
                Thread.sleep(1000);
                System.out.println("Side effect completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Task interrupted.");
            }
        };

        // Predefined result
        String result = "SUCCESS";

        // FutureTask with predefined result
        FutureTask<String> futureTask = new FutureTask<>(task, result);

        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            // Block until the task is done and retrieve the result
            System.out.println("Task Result: " + futureTask.get());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

/*
Runnable vs Callable
1. 是否有返回
2. 是否抛出异常
 */
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("MyRunnable.run() is running");
    }
}

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("Calling MyCallable.call()...");
        return "Hello world!";
    }
}