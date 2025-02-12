package com.ylqi007.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务的辅助工具类
 */
public final class CommonUtils {
    // 私有构造函数，防止实例化
    private CommonUtils() {
        throw new AssertionError("工具类不能实例化");
    }

    // 读取制定路径的文件
    public static String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    // 休眠指定的毫秒数
    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 休眠指定的秒数
    public static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 打印输出带线程信息的日志
    public static void printThreadLog(String message) {
        // 时间戳 | 线程id | 线程名 | 日志信息
        String result = new StringJoiner(" | ")
                .add(String.valueOf(System.currentTimeMillis()))
                .add(String.format("%2d", Thread.currentThread().getId()))
                .add(Thread.currentThread().getName())
                .add(message)
                .toString();
        System.out.println(result);
    }

}
