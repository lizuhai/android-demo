package com.example.zhli.login;

import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etNumber;
    private EditText etPassword;
    private CheckBox cbRemberPwd;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = (EditText) findViewById(R.id.et_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbRemberPwd = (CheckBox) findViewById(R.id.cb_remeber_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);

        // 回显信息
//        Map<String, String> userInfoMap = Utils.getUserInfo(this);
        Map<String, String> userInfoMap = UtilsOfSharedPreferences.getUserInfo(this);
        if(userInfoMap != null) {
            etNumber.setText(userInfoMap.get("number"));
            etPassword.setText(userInfoMap.get("password"));
        }
    }

    @Override
    public void onClick(View v) {
        String number = etNumber.getText().toString();
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cbRemberPwd.isChecked()) {
//            System.out.println("记住密码");
//            boolean isSuccess = Utils.saveUserInfo(this, number, password);
            boolean isSuccess = UtilsOfSharedPreferences.saveUserInfo(this, number, password);
            if(isSuccess)
                Toast.makeText(this,number + ", " + password + " Save ok!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "save fail", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Log ok", Toast.LENGTH_SHORT).show();
    }

}
