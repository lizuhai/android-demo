package com.example.zhli.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhli.mobilesafe.R;
import com.example.zhli.mobilesafe.service.GPSService;

/**
 * Created by zhli on 2015/1/27.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";
    private SharedPreferences sp;
    private LocationManager lm;         // 系统位置服务

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
//        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        for (Object b : objs) {
            String safenumber = sp.getString("safenumber", "");         // 安全号码

            SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
            String sender = sms.getOriginatingAddress();            // 发送者号码
            String body = sms.getMessageBody();

            // 真机上用的时候要打开下面的注释
//            if(!sender.equals(safenumber)) {
                if("#*location*#".equals(body)) {
                    // 得到手机 GPS
                    Log.i(TAG, "GPS");
                    Intent i = new Intent(context, GPSService.class);
                    context.startService(i);
                    SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                    String lastlocation = sp.getString("lastlocation", null);
                    if(TextUtils.isEmpty(lastlocation)) {
                        // 位置没有得到
                        SmsManager.getDefault().sendTextMessage(sender, null, "get location...", null, null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                    }
                    // 中断广播
                    abortBroadcast();
                } else if("#*alarm*#".equals(body)) {
                    // 报警
                    Log.i(TAG, "播放报警音乐");
                    MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setLooping(false);       // 真实中应设为 true
                    player.setVolume(1.0f, 1.0f);   // 音量
                    player.start();

                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(body)) {
                    // 远程锁屏
                    Log.i(TAG, "远程锁屏");
                    abortBroadcast();
                }
//            }
        }
    }
}
