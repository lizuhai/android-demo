package com.example.zhli.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by zhli on 2015/1/22.
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SMS come");
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object o : objs) {
            // 得到短信对象
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();
            System.out.println("body: " + body);
            System.out.println("sender: " + sender);

            // 终止当前广播
            abortBroadcast();       // 拦截短信
        }
    }
}
