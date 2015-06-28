package com.example.zhli.marrymust;

import android.content.ContentValues;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 停 30 向系统短信库中添加一条信息
    new Thread(new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(10 * 1000);
            Uri uri = Uri.parse("content://sms/");  //操作 sms 的 uri
            ContentValues values = new ContentValues();
            values.put("address", "95555");
            values.put("type", "1");
            values.put("body", "get 10000$");
            getContentResolver().insert(uri, values);
        }
    }).start();
}
}

