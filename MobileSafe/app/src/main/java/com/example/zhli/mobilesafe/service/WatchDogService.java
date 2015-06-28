package com.example.zhli.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.zhli.mobilesafe.EnterPwdActivity;
import com.example.zhli.mobilesafe.db.dao.AppLockDao;

import java.util.List;
import java.util.concurrent.TransferQueue;

/**
 * 监视系统的运行状态
 */
public class WatchDogService extends Service {

    private static final String TAG = "WatchDogService";
    private ActivityManager am;
    private boolean flag;
    private AppLockDao dao;
    private InnerReceiver innerReceiver;
    private String tempStopProtectPackname;
    private ScreenOffReceiver offReceiver;

    private List<String> protectPacknames;   // 保护的包名集合
    private Intent intentToPwd;
    private DataChangeReceiver dataChangeReceiver;

    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        offReceiver = new ScreenOffReceiver();
        registerReceiver(offReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver, new IntentFilter("com.example.zhli.mobilesafe.tempstop"));
        dataChangeReceiver = new DataChangeReceiver();
        registerReceiver(dataChangeReceiver, new IntentFilter("com.example.zhli.mobilesafe.applockchange"));

        flag = true;
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dao = new AppLockDao(this);
        protectPacknames = dao.queryAll();
        Log.i(TAG + 1, protectPacknames.toString());

        // 看门狗要弹出一个输入密码的界面
        intentToPwd = new Intent(getApplicationContext(), EnterPwdActivity.class);
        // 服务是没有任务栈信息的，在服务开启 activity，要指定这个 activity 运行的任务栈(新任务栈)
        intentToPwd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    List<RunningTaskInfo> infos = am.getRunningTasks(1);    // 获取最近一个任务栈的任务
                    String packname = infos.get(0).topActivity.getPackageName();
//                    Log.i("WatchDogService", "当前用户正在操作：" + packname); // 打印日志会消耗资源
//                    if(dao.query(packname)) {   // 每次查询数据库太慢了，改成查内存
                    if (protectPacknames.contains(packname)) {    // 查询内存效率高很多
//                        // 判断这个应用是否需要临时停止保护
                        if (packname.equals(tempStopProtectPackname)) {

                        } else {
                            intentToPwd.putExtra("packname", packname);  // 设置要保护程序的包名
                            startActivity(intentToPwd);
                        }
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        flag = false;
        unregisterReceiver(innerReceiver);
        innerReceiver = null;

        unregisterReceiver(offReceiver);
        offReceiver = null;
        unregisterReceiver(dataChangeReceiver);
        dataChangeReceiver = null;
        super.onDestroy();
    }

    private class DataChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "数据库的内容变化了");
            protectPacknames = dao.queryAll();
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("WatchDogService", "临时停止");
            tempStopProtectPackname = intent.getStringExtra("packname");
        }
    }

    /**
     * 锁屏
     */
    private class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("", "锁屏了锁屏了");
            // 锁屏时候清空临时包名，再次进入需要密码
            tempStopProtectPackname = null;
        }
    }
}
