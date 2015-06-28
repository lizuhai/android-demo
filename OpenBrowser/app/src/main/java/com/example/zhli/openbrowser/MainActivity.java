package com.example.zhli.openbrowser;

import android.content.Intent;
import android.net.Uri;
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


    public void openBrowser(View v) {
        // android 内置浏览器的源代码部分文件
//        <action android:name="android.intent.action.VIEW" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <category android:name="android.intent.category.BROWSABLE" />
//        <data android:scheme="http" />
//        <data android:scheme="https" />
//        <data android:scheme="about" />
//        <data android:scheme="javascript" />

        // 显示意图 --> 只能用默认的浏览器（没有默认的就会有异常，不推荐）
//        Intent intent = new Intent();
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");

        // 显示意图 --> 可以让用户选择浏览器
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setData(Uri.parse("http://www.baidu.com"));

        startActivity(intent);
    }
}
