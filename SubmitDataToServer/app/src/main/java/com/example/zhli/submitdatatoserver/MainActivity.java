package com.example.zhli.submitdatatoserver;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText etName;
    private EditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
    }


    public void doGet(View v) {
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();

        // 开启子线程执行任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 用 get 方式发送数据
                final String state = NetworkUtil.loginOfGet(username, password);

                // 主线程程中执行任务
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, state, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void doPost(View v) {
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();

        // 开启子线程执行任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 用 get 方式发送数据
                final String state = NetworkUtil.loginOfPost(username, password);

                // 主线程程中执行任务
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, state, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    /**
     * 使用 httpclicent 方式提交请求
     * @param v
     */
    public void doHttpClientGet(View v) {
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();
        // 开启子线程执行任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetworkUtil2.loginOfGet(username, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, state, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    /**
     * 使用 httpclicent 方式提交请求
     * @param v
     */
    public void doHttpClientPost(View v) {
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();
        // 开启子线程执行任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String state = NetworkUtil2.loginOfPost(username, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, state, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
