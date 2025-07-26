package com.ylqi007;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureFastDemo {

    @Test
    public void test1() {
        CompletableFuture<String> playerA = CompletableFuture.supplyAsync(() -> {
            System.out.println("PlayerA is coming");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Player A";
        });

        CompletableFuture<String> playerB = CompletableFuture.supplyAsync(() -> {
            System.out.println("PlayerB is coming");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Player B";
        });

        CompletableFuture<String> stringCompletableFuture = playerA.applyToEither(playerB, f -> {
            return f + " is winner";
        });

        System.out.println(Thread.currentThread().getName() + "----" + stringCompletableFuture.join());
    }
}
