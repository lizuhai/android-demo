package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.zhli.mobilesafe.ui.SettingSIMBindView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingSIMBindView ssv_setup2_sim;
    private TelephonyManager tm;        // 读取 SIM 的信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        ssv_setup2_sim = (SettingSIMBindView) findViewById(R.id.ssv_setup2_sim);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            // 没有绑定
            ssv_setup2_sim.setChecked(false);
        } else {
            // 已经绑定
            ssv_setup2_sim.setChecked(true);
        }

        ssv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if(ssv_setup2_sim.isChecked()) {
                    editor.putString("sim", null);
                    editor.commit();

                    ssv_setup2_sim.setChecked(false);
                } else {
                    String sim = tm.getSimSerialNumber();
                    editor.putString("sim", sim);

                    ssv_setup2_sim.setChecked(true);
                }
                editor.commit();
            }
        });
    }

    @Override
    protected void showNext() {
        // 是否绑定 SIM 卡
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            Toast.makeText(this, "亲，SIM 卡没有绑定哦", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void showPre() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        // 改变页面切换效果(要在finish() 或者 startActivity()后面执行)
        overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
    }

}
