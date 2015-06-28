package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences sp;           // 保存
    private CheckBox cb_proteting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        cb_proteting = (CheckBox) findViewById(R.id.cb_protecting);

        boolean  protecting = sp.getBoolean("protecting", false);
        if(protecting){
            //手机防盗已经开启了
            cb_proteting.setText("手机防盗已经开启");
            cb_proteting.setChecked(true);
        }else{
            //手机防盗没有开启
            cb_proteting.setText("手机防盗没有开启");
            cb_proteting.setChecked(false);

        }

        cb_proteting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    cb_proteting.setText("手机防盗已经开启");
                }else{
                    cb_proteting.setText("手机防盗没有开启");
                }

                //保存选择的状态
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    @Override
    protected void showNext() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("configed", true);
        editor.commit();

        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();
        // 改变页面切换效果(要在finish() 或者 startActivity()后面执行)
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

}
