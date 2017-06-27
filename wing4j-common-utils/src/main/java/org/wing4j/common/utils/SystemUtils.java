package org.wing4j.common.utils;

import java.lang.management.ManagementFactory;

/**
 * Created by wing4j on 2017/3/7.
 * 8:14
 */
public class SystemUtils {
    /**
     * 获取当前JVM进程号
     * @return 进程号
     */
    public static String pid() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return processName.split("@")[0];
    }
}