package com.example.zhli.phonelistener;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startListen(View v) {
        // 开启服务
        Intent intent = new Intent(this, PhoneListerService.class);
        startService(intent);
    }
    public void stopListen(View v) {
        // 停止服务
        Intent intent = new Intent(this, PhoneListerService.class);
        stopService(intent);
    }
}
