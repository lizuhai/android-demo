package com.example.zhli.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.zhli.mobilesafe.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务方法，提供手机里面所有的应用程序信息
 * Created by zhli on 2015/1/30.
 */
public class AppInfoProvider {
    /**
     * 获取所有的安装的应用程序的信息
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        // 没有包管理器也能做，用户自己安装的apk在/data/app，系统的包在/system/app
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);      // 所有安装在系统上的应用程序包信息
        for (PackageInfo packInfo : packInfos) {
            AppInfo appInfo = new AppInfo();
            // packInfo --> 相当于一个 app 的清单文件
            String packageName = packInfo.packageName;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            String name = packInfo.applicationInfo.loadLabel(pm).toString();
            int flags = packInfo.applicationInfo.flags; // 应用程序的小标记
            if((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 用户程序
                appInfo.setUserApp(true);
            } else {
                // 系统程序
                appInfo.setUserApp(false);
            }
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
                // 安装在手机内存
                appInfo.setInRom(true);
            } else {
                // 安装在存储卡
                appInfo.setInRom(false);
            }

            appInfo.setPackname(packageName);
            appInfo.setName(name);
            appInfo.setIcon(icon);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
