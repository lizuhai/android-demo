package com.example.zhli.mobilesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class CleanCacheActivity extends ActionBarActivity {

    private ProgressBar pb;
    private TextView tv_scan_cache_status;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);

        pb = (ProgressBar) findViewById(R.id.pb);
        tv_scan_cache_status = (TextView) findViewById(R.id.tv_scan_cache_status);

        scanCache();
    }

    /**
     * 扫描手机里面所有应用程序的缓存信息
     */
    private void scanCache() {
        pm = getPackageManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PackageInfo> packInfos = pm.getInstalledPackages(0);
                for (PackageInfo info : packInfos) {
                    // @hide    pm.getPackageSizeInfo();
//                    PackageManager.class.getMet
                }
            }
        }).start();
    }

}
