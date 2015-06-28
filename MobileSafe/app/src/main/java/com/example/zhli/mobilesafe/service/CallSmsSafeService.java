package com.example.zhli.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.zhli.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

    private static final String TAG = "CallSmsSafeService";
    private InnerSmsReceiver receiver;
    private BlackNumberDao dao;
    private TelephonyManager tm;
    private MyListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "短信到来了——内部广播接收者");
            // 检查是否是黑名单号码,短信拦截
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                String result = dao.queryForMode(sender);
                if("2".equals(result) || "3".equals(result)) {
                    Log.i(TAG, "拦截短信");
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        // 使用代码方式注册广播接收者
        receiver = new InnerSmsReceiver();
        registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        dao = new BlackNumberDao(this);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // 使用代码方式反注册广播接收者
        unregisterReceiver(receiver);
        receiver = null;

        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String result = dao.queryForMode(incomingNumber);
                    if ("1".equals(result) || "3".equals(result)) {
                        Log.i(TAG, "挂断电话");
                        // 观察呼叫数据库内容的变化
                        getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"),
                                true, new CallLogObserver(incomingNumber, new Handler()));
//                        endCall();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 挂断电话(android studio 无法生成)
     */
   /*
   private void endCall() {
        try {
            Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony.Stub.asInterface(iBinder).endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * 用内容提供者删除呼叫记录
     */
    private void deleteCallLog(String incomeNumber) {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://call_log/calls");    // 呼叫记录的 uri 路径
        resolver.delete(uri, "number = ?", new String[] {incomeNumber});
    }

    private class CallLogObserver extends ContentObserver {
        private String incomeNumber;
        public CallLogObserver(String incomeNumber, Handler handler) {
            super(handler);
            this.incomeNumber = incomeNumber;
        }
        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG, "数据库内容发生了变化，产生了新的呼叫记录");
            // 删除呼叫记录——储存在联系人的数据库中的
            deleteCallLog(incomeNumber);
            getContentResolver().unregisterContentObserver(this);
            super.onChange(selfChange);
        }
    }
}
