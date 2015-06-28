package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Setup3Activity extends BaseSetupActivity {

    private EditText et_setup3_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);

        // 自动填充上次设置的安全号码
        et_setup3_phone.setText(sp.getString("safenumber", ""));
    }

    @Override
    protected void showNext() {
        // 保存安全号码
        String phone = et_setup3_phone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "安全号码还没有设置", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenumber", phone);
        editor.commit();

        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();

        // 改变页面切换效果(要在finish() 或者 startActivity()后面执行)
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

    /**
     * 选择联系人
     * @param v
     */
    public void selectContact(View v) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * 回掉的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;
        String phone = data.getStringExtra("phone").replace("-", "");
        et_setup3_phone.setText(phone);
    }
}
