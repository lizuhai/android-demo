package com.example.zhli.sender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhli on 2015/1/22.
 */
public class Level2Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = getResultData();
        System.out.println("市 department got message from province: " + message);

        setResultData("give 100$");
    }
}
