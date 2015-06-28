package com.example.zhli.police;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zhli on 2015/1/22.
 */
public class PoliceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I got, I'am a police", Toast.LENGTH_SHORT).show();
    }
}
