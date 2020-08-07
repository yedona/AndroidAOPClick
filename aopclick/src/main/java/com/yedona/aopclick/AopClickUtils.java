package com.yedona.aopclick;

public class AopClickUtils {


    static boolean isFilter = true;

    static long sCheckTime = 500L;

    /**
     * 开始拦截
     */
    public static void start() {
        isFilter = true;
    }

    /**
     * 停止拦截
     */
    public static void stop() {
        isFilter = false;
    }


    /**
     * 设置两次点击事件之间的间隔
     */
    public static void setCheckTime(long sCheckTime) {
        AopClickUtils.sCheckTime = sCheckTime;
    }
}
