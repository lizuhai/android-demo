package com.example.zhli.messageassitent;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private EditText etContent;
    private EditText etNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = (EditText) findViewById(R.id.et_content);
        etNumber = (EditText) findViewById(R.id.et_number);
    }


    public void selectSms(View v) {
        Intent intent = new Intent(this, ListSmsActivity.class);
//        startActivity(intent);
        // 开启一个新的界面，并获取返回值
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            String smsInfo = data.getStringExtra("smsInfo");
            etContent.setText(smsInfo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void sendSms(View v) {
        String content = etContent.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        SmsManager.getDefault().sendTextMessage(number, null, content, null, null);
    }

}
