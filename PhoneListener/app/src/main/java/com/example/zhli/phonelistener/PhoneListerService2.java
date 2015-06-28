package com.example.zhli.phonelistener;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhli on 2015/1/23.
 */
public class PhoneListerService2 extends Service {
    // 电话管理器
    private TelephonyManager tm;
    // 监听对象
    private MyListener listener;
    // 录音机
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务创建时调用的方法
     */
    @Override
    public void onCreate() {
        // 后台获取电话的呼叫状态
            // 得到电话管理器
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    private class MyListener extends PhoneStateListener{
        /**
         * 当电话呼叫状态发生变化
         * @param state
         * @param incomingNumber
         */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            System.out.println("电话状态改变了..." + state);
            // state: 0：空闲状态，1：响铃状态，2：接听状态
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:      // 0 空闲状态
                        if (mediaRecorder != null) {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;

                            System.out.println("录制完毕，上传到服务器");
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:   // 1 响铃状态
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:   // 2 通话状态
                        // 开录音机录音(参见 api Performing Audio Capture 文档 9 步)
                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        File f = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".3gp");
                        mediaRecorder.setOutputFile(f.getAbsolutePath());
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务销毁时调用的方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destory listener");
        // 开启另外一个服务
        Intent i = new Intent(this, PhoneListerService.class);
        startService(i);

        // 取消电话的监听状态
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
    }
}
