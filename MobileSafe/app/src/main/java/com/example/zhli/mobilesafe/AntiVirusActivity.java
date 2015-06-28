package com.example.zhli.mobilesafe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhli.mobilesafe.db.dao.AntivirsuDao;
import com.example.zhli.mobilesafe.utils.FileMd5;

import java.io.File;
import java.util.List;


public class AntiVirusActivity extends ActionBarActivity {

    private static final int SCANNING = 0;
    private static final int SCAN_FINISH = 1;
    private ImageView iv_scan;
    private ProgressBar progressBar;
    private PackageManager pm;
    private TextView tv_scan_status;
    private LinearLayout ll_container;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ScanInfo scanInfo;
            switch (msg.what) {
                case SCANNING:
                    scanInfo = (ScanInfo) msg.obj;
                    tv_scan_status.setText("正在扫描：" + scanInfo.name);
                    TextView tv = new TextView(getApplicationContext());
                    if (scanInfo.isvirus) {
                        tv.setTextColor(Color.RED);
                        tv.setText("发现病毒：" + scanInfo.name);
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setText("扫描安全：" + scanInfo.name);
                    }
                    ll_container.addView(tv, 0);    // 每次加载到最上面
                    break;
                case SCAN_FINISH:
                    tv_scan_status.setText("扫描完毕");
//                    iv_scan.clearAnimation();       // 取消动画
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);

        iv_scan = (ImageView) findViewById(R.id.iv_icon);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

//        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ra.setDuration(1000);
//        ra.setRepeatCount(Animation.INFINITE);
//        iv_scan.startAnimation(ra);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        // 扫描病毒
        scanVarus();
    }

    /**
     * 扫描病毒
     */
    private void scanVarus() {
        pm = getPackageManager();
        tv_scan_status.setText("正在初始化杀毒引擎，请稍后...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<PackageInfo> infos = pm.getInstalledPackages(0);

                progressBar.setMax(infos.size());
                int progress = 0;
                for (PackageInfo info : infos) {
//              String dataDir = info.applicationInfo.dataDir;      // 数据路径
                    String sourceDir = info.applicationInfo.sourceDir;  // 源代码路径(APK 完整路径)
//                  System.out.println("---------------------------------");
//                  System.out.println(dataDir);
//                  System.out.println(sourceDir);
                    String md5 = FileMd5.md5Values(sourceDir);
//                  System.out.println(info.applicationInfo.loadLabel(pm) + " md5:" + md5);
                    ScanInfo scanInfo = new ScanInfo();
                    scanInfo.name = info.applicationInfo.loadLabel(pm).toString();
                    scanInfo.packname = info.packageName;
                    System.out.println(scanInfo.packname + ": " + md5);
                    // 查询 md5 信息是否在病毒数据库中
                    if (AntivirsuDao.isVirus(md5)) {
                        // 发现病毒
                        scanInfo.isvirus = true;
                    } else {
                        // 安全
                        scanInfo.isvirus = false;
                    }
                    Message msg = Message.obtain();
                    msg.what = SCANNING;
                    msg.obj = scanInfo;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(130);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(++progress);
                }
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }).start();


    }

    /**
     * 扫描信息的内部类
     */
    class ScanInfo {
        String packname;
        String name;
        boolean isvirus;
    }

}
