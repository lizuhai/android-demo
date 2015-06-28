package com.example.zhli.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.zhli.mobilesafe.R;
import com.example.zhli.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {

    private static final String TAG = "AddressService";
    private TelephonyManager tm;  // 监听来电
    private MyPhoneStateListener listener;
    private OutCallReceiver receiver;   // 去电广播接收者
    private WindowManager wm;       // 窗体管理员
    private View view;
    private SharedPreferences sp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 监听一个来电
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        // 实例化窗体
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 用代码来注册去电广播接收者
        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter();       // 意图匹配器
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消监听来电
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;

        // 用代码取消去电广播接收者
        unregisterReceiver(receiver);
        receiver = null;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:   // 铃声响起时候
                    // 根据来电号码查询归属地, 并 Toast 显示
                    String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
//                    Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
                    myToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:      // 电话的空闲状态：挂电话之后或来电拒绝
                    // 移除 myToast
                    if(view != null)
                        wm.removeView(view);
                    break;
                default:
                    break;
            }
        }
    }

    private WindowManager.LayoutParams params;
    long[] mHits = new long[2];     // 2：双击事件，换成 3 就表示三击事件

    /**
     * 自定义 Toast
     * @param address 要 Toast 显示的文本
     */
    public void myToast(String address) {
//        view = new TextView(getApplicationContext());
//        view.setText(address);
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
        view = View.inflate(this, R.layout.address_show, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_address);

        // 双击水平居中
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "双击了");
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    params.x = (wm.getDefaultDisplay().getWidth() - view.getWidth()) / 2;
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("lastx", params.x);
                    editor.commit();
                }
            }
        });

        // 拖动窗体效果
        view.setOnTouchListener(new View.OnTouchListener() {
            // 定义手指的初始位置
            int startX;
            int startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        Log.i(TAG, "开始位置为：" + startX + ", " + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
//                        Log.i(TAG, "新的位置为：" + newX + ", " + newY);
                        int dx = newX - startX;
                        int dy = newY - startY;
//                        Log.i(TAG, "手指的偏移量为：" + dx + ", " + dy);
//                        Log.i(TAG, "更新 imageView 的位置：" + newX + ", " + newY);
                        params.x += dx;
                        params.y += dy;

                        // 考虑到边界问题
                        if(params.x < 0)
                            params.x = 0;
                        if (params.y < 0)
                            params.y = 0;
                        if (params.x > wm.getDefaultDisplay().getWidth() - view.getWidth())
                            params.x = wm.getDefaultDisplay().getWidth() - view.getWidth();
                        if (params.y > wm.getDefaultDisplay().getHeight() - view.getHeight())
                            params.y = wm.getDefaultDisplay().getHeight() - view.getHeight();

                        wm.updateViewLayout(view, params);
                        // 重新初始化手指的开始结束位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录控件的坐标
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", params.x);
                        editor.putInt("lasty", params.y);
                        editor.commit();
                        break;

                }
//                return true;    // 事件处理完毕，不要让别的事件或布局响应触摸事件了
                return false;       // 拖动事件基础上实现双击事件要返回 false
            }
        });

        // "半透明", "活力橙", "卫视蓝", "金属灰", "苹果绿"
        int[] ids = {R.drawable.half_alpha, R.drawable.orangle, R.drawable.blue, R.drawable.gray, R.drawable.green};
        sp = getSharedPreferences("config", MODE_PRIVATE);
        view.setBackgroundResource(ids[sp.getInt("which", 0)]);
        textView.setText(address);

        // 窗体的参数
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = sp.getInt("lastx", 20);     // 根据上面的左上显示，所以据左边 100 px
        params.y = sp.getInt("lasty", 200);     // 根据上面的左上显示，所以据上边 100 px
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // android 系统里面具有电话优先级的一种窗体类型(要有权限 SYSTEM_ALERT_WINDOW)(优先级最高，可以显示在所有窗体的最上面)
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

        wm.addView(view, params);
    }

    /**
     * 广播接收者可以做成内部类，所以也可以用代码来注册广播接收者
     */
    private class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData(); // 去电的电话号码
            String address = NumberAddressQueryUtils.queryNumber(phone);
//            Toast.makeText(context, address, Toast.LENGTH_LONG).show();
            myToast(address);
        }
    }

}
