package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class EnterPwdActivity extends ActionBarActivity {

    private EditText et_password;
    private String packname;
    private TextView tv_name;
    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);

        et_password = (EditText) findViewById(R.id.et_password);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);

        Intent intent = getIntent();
        packname = intent.getStringExtra("packname");

        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            tv_name.setText(info.loadLabel(pm) + "已经锁定，请输入密码");
            iv_icon.setImageDrawable(info.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void click(View v) {
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 假设密码是111
        if ("111".equals(password)) {
            // 告诉看门狗，密码正确，暂停保护
            // 自定义广播,临时停止保护
            Intent intent = new Intent();
            intent.setAction("com.example.zhli.mobilesafe.tempstop");
            intent.putExtra("packname", packname);
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        <action android:name="android.intent.action.MAIN" />
//        <category android:name="android.intent.category.HOME" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <category android:name="android.intent.category.MONKEY"/>
        // 回到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
        // Home 键执行的是 onstop 方法，必须finish() 不然会保留上次的记录，图标等会乱了

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
