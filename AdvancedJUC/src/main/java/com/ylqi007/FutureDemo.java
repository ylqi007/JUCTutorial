package com.ylqi007;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureDemo {
    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }});
    }
}
