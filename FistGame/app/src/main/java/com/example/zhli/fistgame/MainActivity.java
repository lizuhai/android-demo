package com.example.zhli.fistgame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 默认的横竖屏切换 activity 会被销毁，然后重新创建，所以会满血复活
 * 加上如下配置
 *      android:configChanges="orientation|keyboardHidden|screenSize"
 * 取消生命周期默认的行为
 * OR
 * 直接用android:screenOrientation="landscape"让手机一直横屏，这样也不会有问题
 */
public class MainActivity extends ActionBarActivity {

    private TextView tvBlood;
    private int blood = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBlood = (TextView) findViewById(R.id.tv_blood);
    }

    public void click(View v) {
        blood --;
        if(blood > 0) {
            tvBlood.setText("对方的生命值" + blood);
        } else {
            Toast.makeText(this, "鞭尸", Toast.LENGTH_SHORT).show();
        }
    }

}
