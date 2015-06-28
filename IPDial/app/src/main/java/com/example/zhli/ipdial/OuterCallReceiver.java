package com.example.zhli.ipdial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by zhli on 2015/1/22.
 * 1. 创建一个收音机
 */
public class OuterCallReceiver extends BroadcastReceiver{
    /**
     * 当接受到消息对应的方法
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("out call received");
        String number = getResultData();

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String ipNumber = sp.getString("ipNumber", "");

//        System.out.println(number);
        setResultData(ipNumber + number);
    }
}
