package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureAPI4Demo {

    @Test
    public void test1() {
        CompletableFuture<String> completableFuturePlayerA = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Player A";
        });

        CompletableFuture<String> completableFuturePlayerB = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Player B";
        });

        CompletableFuture<String> result = completableFuturePlayerA.applyToEither(completableFuturePlayerB, f -> f + " is winner");

        System.out.println(Thread.currentThread().getName() + ": " + result.join());    // without join(): main: java.util.concurrent.CompletableFuture@3e3047e6[Not completed]
    }
}
