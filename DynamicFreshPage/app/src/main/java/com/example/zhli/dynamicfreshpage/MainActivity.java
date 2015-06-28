package com.example.zhli.dynamicfreshpage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private LinearLayout llGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llGroup = (LinearLayout) findViewById(R.id.ll);
    }

    public void onClick(View v) {
        // add 一个 TextView 向 llGroup
        TextView tv = new TextView(this);
        // 设置文本内容
        tv.setText("xzli　　　女　　　34");
        // 将 tv 添加到视图中
        llGroup.addView(tv);
    }



}
