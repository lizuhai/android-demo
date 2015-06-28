package com.example.zhli.mobilesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zhli.mobilesafe.service.AddressService;
import com.example.zhli.mobilesafe.service.CallSmsSafeService;
import com.example.zhli.mobilesafe.service.WatchDogService;
import com.example.zhli.mobilesafe.ui.SettingBlackNumberView;
import com.example.zhli.mobilesafe.ui.SettingClickView;
import com.example.zhli.mobilesafe.ui.SettingItemView;
import com.example.zhli.mobilesafe.ui.SettingShowNumberAddressView;
import com.example.zhli.mobilesafe.ui.SettingWatchdogView;
import com.example.zhli.mobilesafe.utils.ServiceUtils;


public class SettingActivity extends ActionBarActivity {

    private SettingItemView siv_update;     // 设置是否开启自动更新
    private SharedPreferences sp;       // 保存设置信息
    private SettingShowNumberAddressView showNumberAddress;     // 设置开启来电归属地显示
    private Intent showAddress;         // 设置开启来电归属地显示意图
    private SettingClickView scv_changebg;      // 设置来电归属地显示框背景色
    private SettingBlackNumberView sbnv_callsms_safe;   // 黑名单拦截设置
    private Intent callSmsSafeIntent;
    private SettingWatchdogView swv_watchdog;   // 看门狗视图
    private Intent watchdogIntent;              // 看门狗意图

    /**
     * 用户重新看到这个界面的时候
     */
    @Override
    protected void onResume() {
        super.onResume();
        showAddress = new Intent(this, AddressService.class);
        // 通过查看服务是否启动来控制 checkbox 的勾选状态
        boolean isRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.example.zhli.mobilesafe.service.AddressService");
        // 监听来电的服务是运行的
        showNumberAddress.setChecked(isRunning);


        boolean isCallSmsServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.example.zhli.mobilesafe.service.CallSmsSafeService");
        showNumberAddress.setChecked(isCallSmsServiceRunning);

        boolean isWatchdogServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this,
                "com.example.zhli.mobilesafe.service.WatchDogService");
        swv_watchdog.setChecked(isWatchdogServiceRunning);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        boolean update = sp.getBoolean("update", false);
        siv_update.setChecked(update);

        // 设置是否开启自动更新
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                siv_update.setChecked(!siv_update.isChecked());
                SharedPreferences.Editor editor = sp.edit();
                if(siv_update.isChecked()) {
                    siv_update.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    siv_update.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });


        // 设置开启来电归属地显示
        showNumberAddress = (SettingShowNumberAddressView) findViewById(R.id.showNumberAddress);
        showAddress = new Intent(this, AddressService.class);

//        // 通过查看服务是否启动来控制 checkbox 的勾选状态
//        boolean isRunning = ServiceUtils.isServiceRunning(this,
//                "com.example.zhli.mobilesafe.service.AddressService");
//        // 监听来电的服务是运行的
//        showNumberAddress.setChecked(isRunning);

        showNumberAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showNumberAddress.isChecked()) {
                    stopService(showAddress);
                    showNumberAddress.setChecked(false);
                } else {
                    startService(showAddress);
                    showNumberAddress.setChecked(true);
                }
            }
        });

        // 设置号码归属地的背景
        final String[] items = {"半透明", "活力橙", "卫视蓝", "金属灰", "苹果绿"};
        scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
        scv_changebg.setTitle("归属地提示框风格");
        int which = sp.getInt("which", 0);
        scv_changebg.setDesc(items[which]);

        scv_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("归属地提示框风格");
                builder.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 保存参数
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which", which);
                        editor.commit();
                        scv_changebg.setDesc(items[which]);
                        // 取消对话框
                        dialog.dismiss();
                    }
               });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });


        // 设置黑名单拦截
        sbnv_callsms_safe = (SettingBlackNumberView) findViewById(R.id.sbnv_callsms_safe);
        callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);

        sbnv_callsms_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sbnv_callsms_safe.isChecked()) {
                    stopService(callSmsSafeIntent);
                    sbnv_callsms_safe.setChecked(false);
                } else {
                    startService(callSmsSafeIntent);
                    sbnv_callsms_safe.setChecked(true);
                }
            }
        });


        // 设置程序锁
        swv_watchdog = (SettingWatchdogView) findViewById(R.id.swv_watchdog);
        watchdogIntent = new Intent(this, WatchDogService.class);
        swv_watchdog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swv_watchdog.isChecked()) {
                    stopService(watchdogIntent);
                    swv_watchdog.setChecked(false);
                } else {
                    startService(watchdogIntent);
                    swv_watchdog.setChecked(true);
                }
            }
        });
    }
}
