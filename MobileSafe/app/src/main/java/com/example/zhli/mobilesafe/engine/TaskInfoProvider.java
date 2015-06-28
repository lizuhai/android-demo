package com.example.zhli.mobilesafe.engine;

import android.app.ActivityManager;
import android.app.ActivityManager.*;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.zhli.mobilesafe.R;
import com.example.zhli.mobilesafe.domain.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供手机的进程信息
 * Created by zhli on 2015/1/31.
 */
public class TaskInfoProvider {
    public static List<TaskInfo> getTaskInfos(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<>();
        for (RunningAppProcessInfo info : processInfos) {
            TaskInfo taskInfo = new TaskInfo();
            String packname = info.processName;
            taskInfo.setPackname(packname);
            Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{info.pid});
            long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024;
            taskInfo.setMemsize(memsize);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                String name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setIcon(icon);
                taskInfo.setName(name);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 用户进程
                    taskInfo.setUserTask(true);
                } else {
                    // 系统进程
                    taskInfo.setUserTask(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // linux 内核和播放器都是用c代码写的，没有apk包，所以会出现找不到名字图片的异常
                taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
                taskInfo.setName(packname);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

}
