package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class LostFindActivity extends ActionBarActivity {

    private SharedPreferences sp;
    private TextView tv_safenumber;
    private ImageView iv_protecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置向导页
        sp = getSharedPreferences("config", MODE_PRIVATE);
            // 判断是否做过设置向导
        boolean configed = sp.getBoolean("configed", false);
//        setContentView(R.layout.activity_lost_find);
        if(configed) {
            setContentView(R.layout.activity_lost_find);
            tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
            iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
            // 得到设置过的安全号码
            String safenumber = sp.getString("safenumber", "");
            tv_safenumber.setText(safenumber);
            //设置防盗保护的状态
            boolean protecting = sp.getBoolean("protecting", false);
            if(protecting){
                //已经开启防盗保护
                iv_protecting.setImageResource(R.drawable.lock);
            }else{
                //没有开启防盗保护
                iv_protecting.setImageResource(R.drawable.unlock);
            }
        } else {
            // 跳转到向导页面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            finish();
        }
    }

    /**
     * 重新进入防盗设置向导页
     */
    public void reEnterSetup(View v) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
