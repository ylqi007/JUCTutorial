package com.ylqi007._07_get_join;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetOrJoinDemo {
    public static void main(String[] args) {
        // get() or join()
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "hello";
        });

        try {
            CommonUtils.printThreadLog("future.get() = " + future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        /*
        join()
            * @throws CancellationException if the computation was cancelled
            * @throws CompletionException if this future completed
         */
        System.out.println("future.join() = " + future.join());
    }
}
