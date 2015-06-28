package com.example.zhli.mobilesafe.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 系统信息工具类
 * Created by zhli on 2015/1/30.
 */
public class SystemInfo {
    /**
     * 获取正在运行的进程数量
     * @param context 上下文
     * @return 正在运行的进程数量
     */
    public static int getRunningProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }

    /**
     * 获取手机的剩余可用内存
     */
    public static long getAvailMem(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 获取手机的全部内存
     * 只能在 4.0 以上才可以这么写
     */
    public static long getTotalMemHighVersion(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.totalMem;  // 只能在 4.0 以上才可以这么写
    }

    /**
     * 获取手机的全部内存
     * @return long byte
     */
    public static long getTotalMem(Context context) {
        try {
            File file = new File("/proc/meminfo");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            // MemTotal:           513000 kB
            for (char c : line.toCharArray())
                if (c >= '0' && c <= '9')
                    sb.append(c);
            return Long.parseLong(sb.toString()) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
