package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.zhli.mobilesafe.service.AutoCleanService;
import com.example.zhli.mobilesafe.utils.ServiceUtils;


public class TaskSettingActivity extends ActionBarActivity {

    private CheckBox cb_show_system;
    private CheckBox cb_auto_clean;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        cb_auto_clean = (CheckBox) findViewById(R.id.cb_auto_clean);
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        // 回显
        cb_show_system.setChecked(sp.getBoolean("showsystem", false));

        // 保存
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Editor editor = sp.edit();
                editor.putBoolean("showsystem", isChecked);
                editor.commit();
            }
        });

        cb_auto_clean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 锁屏的广播事件是一个特殊的广播事件，在清单文件中配置是不会生效的
                // 只能在代码里面注册才会生效
                // 写到服务里面
                Intent intent = new Intent(TaskSettingActivity.this, AutoCleanService.class);
                if (isChecked) {
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
    }

    /**
     * 锁屏清理要在服务运行时候保持checkbox勾选状态
     */
    @Override
    protected void onStart() {
        boolean running = ServiceUtils.isServiceRunning(this, "com.example.zhli.mobilesafe.service.AutoCleanService");
        cb_auto_clean.setChecked(running);
        super.onStart();
    }
}
