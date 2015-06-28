package com.zhli.handlerdemo;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        /**
         * 子线程使用 handler 的步骤：
         * 1. Looper.prepare(); 先调用，并且只能执行一次，
         * 2. 创建并使用 handler
         * 3. Looper.loop();
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1
                Looper.prepare();
                // 2.1
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        System.out.println(msg.what);
                    }
                };
                // 2.2
                /**
                 * 最终将 message 发送到 messageQueue 里面
                 * 在 messageQueue里面，所有的message 按照执行的时间顺序排列
                 */
                handler.sendEmptyMessage(0);
                // 3
                /**
                 * 依次取出 messageQueue 里面的 message，并执行 message.target.dispatchMessage(), 其中message.target就是 handler
                 */
                Looper.loop();
            }
        }).start();
    }


}
