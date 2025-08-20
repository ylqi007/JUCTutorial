package com.ylqi007.chap02completablefuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class CompletableFutureAPI4ApplyToEitherTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3}) // Player B 的等待时间
    public void testApplyToEither(int playerBSleepSeconds) {
        CompletableFuture<String> playerA = CompletableFuture.supplyAsync(() -> {
            System.out.println("Player A comes in");
            threadSleep(2);
            return "Player A";
        });

        CompletableFuture<String> playerB = CompletableFuture.supplyAsync(() -> {
            System.out.println("Player B comes in");
            threadSleep(playerBSleepSeconds); // 1s, 2s, 3s
            return "Player B";
        });

        CompletableFuture<String> result = playerA.applyToEither(playerB, f -> f + " is winner");

        System.out.printf("B sleep %ds → %s%n", playerBSleepSeconds, result.join());

        System.out.println(Thread.currentThread().getName() + ": " + result.join());    // without join(): main: java.util.concurrent.CompletableFuture@3e3047e6[Not completed]
    }


    @Test
    public void testApplyToEitherAsync() {
        CompletableFuture<String> playerA = CompletableFuture.supplyAsync(() -> {
            System.out.println("Player A comes in");
            threadSleep(1);
            return "Player A";
        });

        CompletableFuture<String> playerB = CompletableFuture.supplyAsync(() -> {
            System.out.println("Player B comes in");
            threadSleep(1); // 1s, 2s, 3s
            return "Player B";
        });

        CompletableFuture<String> result = playerA.applyToEither(playerB, f -> f + " is winner");

        System.out.println(Thread.currentThread().getName() + ": " + result);    // without join(): main: java.util.concurrent.CompletableFuture@3e3047e6[Not completed]
    }


    private void threadSleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
