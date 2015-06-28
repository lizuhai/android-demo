package com.example.zhli.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by zhli on 2015/1/27.
 * 开机启动检测 SIM 卡是否变更
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        boolean protecting = sp.getBoolean("protecting", false);
        // 开启了防盗保护
        if (protecting) {
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // 读取之前保存的 SIM 卡信息
            String savedSim = sp.getString("sim", "")+"a";
            // 读取当前的 SIM 卡信息
            String realSim = tm.getSimSerialNumber();
            // 比较两次的 SIM 卡是否一样，不一样给安全号码发送短信
            if(!TextUtils.isEmpty(savedSim) && savedSim.equals(realSim)) {
                // sim 卡没有变更
            } else {
                System.out.println("SIM 卡已经变更");
                Toast.makeText(context, "", Toast.LENGTH_LONG).show();
                SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim changing....\n SIM 序列号:" + realSim, null, null);
            }
        }
    }
}
