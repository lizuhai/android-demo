package com.example.zhli.xutilsdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private EditText etPath;
    private TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPath = (EditText) findViewById(R.id.et_path);
        tvInfo = (TextView) findViewById(R.id.tv_info);
    }

    public void download(View v) {
        String path = etPath.getText().toString().trim();
        if(TextUtils.isEmpty(path)) {
            Toast.makeText(this, "fill the path", Toast.LENGTH_SHORT).show();
            return;
        } else {

            HttpUtils http = new HttpUtils();
            HttpHandler handler = http.download(path,       // 文件下载路径
                    "/sdcard/xxx.exe",                   // 文件保存位置
                    true,                                   // 支持断点续传
                    true,                                   // 若从请求信息中读到文件名，下载完成后自动重命名
                    new RequestCallBack<File>() {
                        @Override
                        public void onStart() {
                            tvInfo.setText("conn...");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            tvInfo.setText(current + "/" + total);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                            tvInfo.setText("downloaed: " + fileResponseInfo.result.getPath());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            tvInfo.setText(s);
                        }
                    });

            // 停止下载
//            handler.stop();
        }
    }

}
