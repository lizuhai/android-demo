package com.example.zhli.mobilesafe;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class TrafficManagerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_manager);

        // 流量统计

        // 1. 得到包管理器
        PackageManager pm = getPackageManager();
        // 2. 遍历手机操作系统，获取所有应用的pid
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applicationInfos) {
            int uid = applicationInfo.uid;
            // 也可以通过 new File("/proc/uid_stat/" + uid + "/tcp_rcv");    // 下载
            // 获得new File("/proc/uid_stat/" + uid + "/tcp_snd");    // 上传
            long tx = TrafficStats.getUidTxBytes(uid);    // 上传量 byte
            long rx = TrafficStats.getUidRxBytes(uid);      // 下载量 byte
            // tx/rx 返回的 -1 表示没有产生流量或不支持流量统计
        }
        TrafficStats.getMobileTxBytes();    // 2G/3G 上传
        TrafficStats.getMobileRxBytes();    // 2G/3G 下载

        TrafficStats.getTotalTxBytes();    // 2G/3G/WIFI 上传
        TrafficStats.getTotalRxBytes();    // 2G/3G/WIFI 下载

    }

}
