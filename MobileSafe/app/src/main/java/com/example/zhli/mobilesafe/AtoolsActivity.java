package com.example.zhli.mobilesafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zhli.mobilesafe.utils.SmsUtils;


public class AtoolsActivity extends ActionBarActivity {

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**
     * 进入号码归属地查询的页面
     */
    public void numberQuery(View v) {
        Intent intent = new Intent(AtoolsActivity.this, NumberAddressActivity.class);
        startActivity(intent);
    }

    /**
     * 点击事件，短信备份
     */
    public void smsBackup(View v) {
        pd = new ProgressDialog(this);
//        final ProgressBar pb = new ProgressBar(this);

        // 更改样式(默认是加载的样式)
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        pd.setMessage("正在为您拼命备份中...");
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SmsUtils.backupSms(getApplicationContext(), new SmsUtils.BackupCallBack() {
                        @Override
                        public void beforeBackup(int max) {
                            pd.setMax(max);
//                            pb.setMax(max);
                        }
                        @Override
                        public void onSmsBackup(int process) {
                            pd.setProgress(process);
//                            pb.setProgress(process);
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this, "备份失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    pd.dismiss();
                }
            }
        }).start();
    }

    /**
     * 点击事件，短信备份还原
     */
    public void smsRestore(View v) {

    }
}
