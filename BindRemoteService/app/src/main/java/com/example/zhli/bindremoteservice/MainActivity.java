package com.example.zhli.bindremoteservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private MyConn conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 绑定远程服务
     * @param v
     */
    public void bind(View v) {
        Intent intent = new Intent();
        intent.setAction("com.zhli.remoteservice");
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * 解除绑定远程服务
     * @param v
     */
    public void unbind(View v) {

    }

    /**
     * 调用远程服务
     * @param v
     */
    public void call(View v) {

    }
}
