package com.ylqi007._05_completablefuture_exception;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

/**
 * 如果回调链中出现任何异常，回调链不会向下执行，立即进入异常处理。
 */
public class ExceptionChainDemo {

    public static void main(String[] args) {
        // 异步任务如何在回调链中传播
        CompletableFuture.supplyAsync(() -> {
            // int r = 1 / 0;      // 回调链中出现异常，转入异常处理
            return "result1";   // 异步任务中直接返回，并不是耗时操作，因此在 main 线程中完成
        }).thenApply(result -> {
            CommonUtils.printThreadLog("result of step 1: " + result);
            String str = null;
            str.length();
            return result + " result2";
        }).thenApply(result -> {
            return result + " result3";
        }).thenAccept(CommonUtils::printThreadLog);
    }
}
