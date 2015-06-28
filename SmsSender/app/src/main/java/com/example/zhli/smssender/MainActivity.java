package com.example.zhli.smssender;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etNo;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNo = (EditText) findViewById(R.id.et_number);
        etContent = (EditText) findViewById(R.id.et_content);
        Button button = (Button) findViewById(R.id.btn_send);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // 电话号码
        String no = etNo.getText().toString();
        // 短息内容
        String content = etContent.getText().toString();

        Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();

        SmsManager smsManager = SmsManager.getDefault();
        // 调用发送短消息功能，所以需要权限.在AndroidManifest.xml中添加 <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
        smsManager.sendTextMessage(
                no, // 收件人
                null,
                content,    // 短息内容
                null,
                null);
    }
}
