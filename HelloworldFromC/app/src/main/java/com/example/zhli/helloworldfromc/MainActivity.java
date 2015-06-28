package com.example.zhli.helloworldfromc;

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

    // 1. 定义一个c的方法的接口
    public native String HelloWorldFromC();

    public void click(View v) {
        // Toast From C code
    }
}
