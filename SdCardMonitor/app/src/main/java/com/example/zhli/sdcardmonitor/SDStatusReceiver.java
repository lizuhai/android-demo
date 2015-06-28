package com.example.zhli.sdcardmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zhli on 2015/1/22.
 */
public class SDStatusReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "sd 卡被移除，微信头像暂时不可用", Toast.LENGTH_SHORT).show();
    }
}
