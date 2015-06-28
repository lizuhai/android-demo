package com.example.zhli.htmlsourcecode;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "Zhli_MainActivity";
    private TextView tvHtml;
    private EditText etUrl;
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    tvHtml.setText((String) msg.obj);
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this, "access failed!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHtml = (TextView) findViewById(R.id.tv_html);
        etUrl = (EditText) findViewById(R.id.et_url);

    }

    public void getHtml(View v) {
        final String url = etUrl.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求方法
                String html = getHtmlFromInternet(url);

                // 更新 TextView 的显示
                if(html != null && html != "") {
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    msg.obj = html;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ERROR;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }

    /**
     * 根据给定的 url 抓取网页源代码
     * @param url
     * @return
     */
    private String getHtmlFromInternet(String url) {
        try {
            URL mURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.connect();         // 这一步可以省略
            int code = conn.getResponseCode();
            if(code == 200) {
                InputStream is = conn.getInputStream();
                String html = getStringFromInputStream(is);

                return html;
            } else {
                Log.i(TAG, "访问失败 " + code);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据流返回一个字符串信息
     * @return
     */
    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String html = baos.toString();      // 把流中的数据转成字符串，采用的是 UTF-8 码
        if(html.contains("gbk") || html.contains("gb2312")
                || html.contains("GBK") || html.contains("GB2312")) {
            // 若以 gbk 编码
            byte[] bytes = baos.toByteArray();
            html = new String(bytes, "gbk");
        }
        baos.close();
        return html;
    }
}
