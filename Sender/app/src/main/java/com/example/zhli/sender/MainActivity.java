package com.example.zhli.sender;

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


    /**
     * 发送广播事件（消息）
     * @param v
     */
    public void send(View v) {
        Intent intent = new Intent();
        // 发送广播一般都是隐式意图
        intent.setAction("com.example.zhli.sender.HelloWorld"); // 自定义广播
        // 无序广播(不可拦截，不可终止)
        sendBroadcast(intent);
    }

    /**
     * 发送有序广播
     * @param v
     */
    public void sendOrder(View v) {
        Intent intent = new Intent();
        // 发送广播一般都是隐式意图
        intent.setAction("com.example.zhli.sender.songwennuan"); // 自定义广播
        // 有序广播（可被拦截，可被终止，可被修改）
        // 参数说明（意图，接收权限，不可被拦截的指定接收者，handler，初始码，初始数据, 额外的信息）
//        sendOrderedBroadcast(intent, null, null, null, 0, "db", null);
        sendOrderedBroadcast(intent, null, new FinalReceiver(), null, 0, "give 10000$", null);
    }
}
