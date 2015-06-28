package com.example.zhli.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

public class ServiceUtils {
    /**
     * 判断服务的运行状态
     * @param context 上下文参数
     * @param serviceName 服务名字
     * @return 正在运行 -> true
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        // 他不光可以管理 activity 还可以管理 Service
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> infos = am.getRunningServices(100);   // 参数100指定 infos 最大可以接收的服务数量
        for(RunningServiceInfo info : infos) {
            String name = info.service.getClassName();    // 得到正在运行服务的名字
            if(serviceName.equals(name))
                return true;
        }
        return false;
    }
}
