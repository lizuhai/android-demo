package com.example.zhli.rpcalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;


public class CalcActivity extends ActionBarActivity {

    private TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvResult = (TextView) findViewById(R.id.tv_result);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        byte[] result = name.getBytes();
        int number = 0;
        for (byte b : result) {
            number += b & 0xff;     // 将二进制的 b 转换成 10 进制
        }
        int score = Math.abs(number) % 100;
        tvResult.setText(name + " RP 为：" + score);
    }



}
