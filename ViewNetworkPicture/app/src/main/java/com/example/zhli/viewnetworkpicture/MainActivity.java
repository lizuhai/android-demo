package com.example.zhli.viewnetworkpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText etUrl;
    private ImageView ivIcon;
    private final int success = 0;
    private final int error = 1;

    private Handler handler = new Handler() {
        /**
         * 接收消息
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "what = " + msg.what);
            if(msg.what == success)     // 当前是访问网络，去显示图片
                ivIcon.setImageBitmap((Bitmap) msg.obj);
            else if(msg.what == error)
                Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        etUrl = (EditText) findViewById(R.id.et_url);
        findViewById(R.id.btn_submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final String url = etUrl.getText().toString();
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        // android 4.0 以上不允许直接在主线程中直接获取网上图片等内容，
        // 防止阻塞主线程，非这么做的话 android.os.NetworkOnMainThreadException
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getImageFromNet(url);

                    // CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views
//                ivIcon.setImageBitmap(bitmap);        // 显示图片

                if(bitmap != null) {
                    Message msg = new Message();
                    msg.what = success;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = error;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    /**
     * 根据 url 抓取网络图片返回
     * @param url
     * @return url对应的图片
     */
    private Bitmap getImageFromNet(String url) {
        HttpURLConnection conn = null;
        try {
            // 创建一个 URL 连接
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");   // GET 请求方式
            conn.setConnectTimeout(10000);  // 设置超时时间 10 s
            conn.setReadTimeout(5000);      // 设置读取数据超时时间

            conn.connect();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                // connect ok
                InputStream is = conn.getInputStream();         // 获得服务器返回的数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); // 根据流常建一个 bitmap 对象
                return bitmap;
            } else {
                Log.i(TAG, "访问失败：responseCode = " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();  //断开连接
            }
        }
        return null;
    }
}
