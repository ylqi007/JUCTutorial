package com.ylqi007.utils;


import org.junit.jupiter.api.Test;

class CommonUtilsTest {

    @Test
    public void testCommonUtils() {
        String newsContent = CommonUtils.readFile("news.txt");

        CommonUtils.printThreadLog(newsContent);    // 1739335967795 |  1 | main | Oh my god! CompletableFuture 真 TMD 好用！
    }

}