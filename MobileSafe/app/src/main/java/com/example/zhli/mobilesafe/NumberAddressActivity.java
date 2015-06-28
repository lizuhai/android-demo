package com.example.zhli.mobilesafe;

import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.db.dao.NumberAddressQueryUtils;


public class NumberAddressActivity extends ActionBarActivity {

    private static final String TAG = "NumberAddressActivity";
    private EditText et_phone;
    private TextView result;
    private Vibrator vibrator;      // 系统的震动服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address);

        et_phone = (EditText) findViewById(R.id.et_phone);
        result = (TextView) findViewById(R.id.result);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().trim();
                if(s != null && s.length() >= 3) {
                    // 查询数据库，并显示结果
                    String address = NumberAddressQueryUtils.queryNumber(s.toString());
                    result.setText(address);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 查询号码归属地
     */
    public void numberAddressQuery(View v) {
        String phone = et_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();

            // 抖动输入框
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_phone.startAnimation(shake);

            // 震动
            vibrator.vibrate(1000); // 震动 1 s
            long[] pattern = {200, 200, 300, 300};  // 成对出现, 震动、停、震动、停
            vibrator.vibrate(pattern, -1);  // -1：不循环，0：从 pattern 的 0 循环，1：从 1 开始循环震动

            return;
        }
        String address = NumberAddressQueryUtils.queryNumber(phone);
        result.setText(address);
        Log.i(TAG, "您要查询的号码：" + phone);
    }
}
