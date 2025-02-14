package com.ylqi007._08_completablefuture_parallelstream;

import com.ylqi007.utils.CommonUtils;

/**
 * 定义一个 MyTask 类，来模拟耗时的长任务
 */
public class MyTask {
    private int duration;

    public MyTask(int duration) {
        this.duration = duration;
    }

    // 模拟耗时的长任务
    public int doWork() {
        CommonUtils.printThreadLog("doWork()...");
        CommonUtils.sleepSeconds(duration);
        return duration;
    }
}
