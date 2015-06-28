package com.example.zhli.phonelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;

/**
 * Created by zhli on 2015/1/23.
 * 手机启动时，自动启动电话监听器的服务
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PhoneStateListener.class);
        context.startService(i);
    }
}
