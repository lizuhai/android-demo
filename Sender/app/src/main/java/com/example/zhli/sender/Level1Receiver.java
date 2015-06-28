package com.example.zhli.sender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhli on 2015/1/22.
 */
public class Level1Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = getResultData();
        System.out.println("province department got message from center: " + message);

        setResultData("give 1000$");
    }
}
