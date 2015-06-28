package com.example.zhli.sender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhli on 2015/1/22.
 */
public class FinalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = getResultData();
        System.out.println("final receiver department got message from town: " + message);

        setResultData("两袋大米");
    }
}
