package com.cp.tencentlivesimple.util;

/**
 * create by apple
 * create on 2020/8/26
 * description
 */
public class TimeUtil {

    /**
     * 毫秒转为 00:00 ，单位为毫秒
     * @param duration
     */
    public static String duration2Time(long duration) {
        long m = duration / 60;
        long s = duration % 60;
        StringBuilder builder = new StringBuilder();
        builder.append(m / 10).append(m % 10).append(":").append(s / 10).append(s % 10);
        return builder.toString();
    }
}
