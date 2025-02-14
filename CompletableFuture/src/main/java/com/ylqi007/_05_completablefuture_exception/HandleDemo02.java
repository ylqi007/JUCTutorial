package com.ylqi007._05_completablefuture_exception;

import com.ylqi007.utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

/**
 * 不管是否出现异常，handle() 方法都会执行。
 * 所以，handle() 核心作用在于对上一步异步任务中进行现场恢复。
 */
public class HandleDemo02 {
    public static void main(String[] args) {
        // handle()
        // 需求：对回调链中的一次异常进行恢复处理。
        CommonUtils.printThreadLog("main() start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // int r = 1 / 0;
            return "result1";
        }).handle((result, ex) -> {
            if (ex != null) {
                CommonUtils.printThreadLog("从上一步异常中恢复。。。");
                // ex.printStackTrace();
                return "Unknown1 | ";
            }
            return result;
        }).thenApply(result -> {
            String str = null;
            str.length();
            return result + " result2";
        }).handle((result, ex) -> {
            if (ex != null) {
                CommonUtils.printThreadLog("从上一步异常中恢复。。。");
                // ex.printStackTrace();
                return "Unknown2 | ";
            }
            return result;
        }).thenApply(result -> {
            return result + " | result3";
        }).thenApply(result -> {
            return result + " | result4";
        });

        CommonUtils.printThreadLog("main() continue");

        CommonUtils.printThreadLog(future.join());

        CommonUtils.printThreadLog("main() end");
    }
}

/*
.handle((result, ex) -> {
            if (ex != null) {
                CommonUtils.printThreadLog("从上一步异常中恢复。。。");
                ex.printStackTrace();
                return "从 handle() 中恢复后，返回的结果";
            }
            return result;
        });
 */
