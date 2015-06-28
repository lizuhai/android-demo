package com.example.zhli.mobilesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.utils.StreamTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends ActionBarActivity {

    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int JSON_ERROR = 3;
    private static final int NETWORK_ERROR = 4;
    private static final int STAY_TIME = 1000;

    private TextView tv_splash_version;
    private TextView tv_update_info;
    private static final String TAG = "SplashActivity";
    private String description;     // apk 新版描述信息
    private String apkurl;          // apk 新版本下载地址
    private SharedPreferences sp;   // 获取设置信息

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATE_DIALOG:    // 显示升级对话框
                    Log.i(TAG, "显示页面升级对话框");
                    showUpdateDialog();
                    break;
                case ENTER_HOME:    // 进入主页面
                    enterHome();
                    break;
                case URL_ERROR:    // url 错误
                    enterHome();
                    Toast.makeText(getApplicationContext(), "url 错误", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:    // 网络异常
                    enterHome();
                    Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR:    // json解析出错
                    enterHome();
                    Toast.makeText(getApplicationContext(), "json解析出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }


    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        // 拷贝数据库
        copyDB("address.db");
        copyDB("antivirus.db");

        boolean update = sp.getBoolean("update", false);
        if(update) {
            // 检查升级
            checkForUpdate();
        } else {
            // 自动升级关闭
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            }, 2000);
        }

        tv_splash_version.setText("版本号：" + getVersionName() + " V");

        // 添加渐变效果
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splish).startAnimation(aa);


        // 创建桌面快捷图标
        installShortCut();
    }

    /**
     * 创建桌面快捷图标(发出一个自定义的广播)
     */
    private void installShortCut() {
        boolean shortcut = sp.getBoolean("shortcut", false);
        if (shortcut)
            return;
        // 发送广播的意图
        Intent intent = new Intent();
        intent.setAction("com.android.launcher2.InstallShortcutReceiver");
        // 要包含三个重要的信息：名称、图标、干什么
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机小卫士");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        // 桌面点击图标对应的意图
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction("android.intent.action.MAIN");
        shortcutIntent.addCategory("android.intent.category.LAUNCHER");
        shortcutIntent.setClassName(getPackageName(), "com.example.zhli.mobilesafe.SplashActivity");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        sendBroadcast(intent);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("shortcut", true);
        editor.commit();
    }

    /**
     * 把 assets/address.db 数据库文件 copy 到 data/data/com.example.zhli.mobilesafe/files/address.db
     */
    private void copyDB(String filePath) {
        // 只要拷贝了一次，就不要再拷贝了
        try {
            InputStream is = getAssets().open(filePath);
            File file = new File(getFilesDir(), filePath);
            if(file.exists() && file.length() > 0) {
                // 正常，不需要拷贝了
                Log.i(TAG, "正常，不需要拷贝了");
            } else {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024 * 2];
                int len = -1;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                }
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setCancelable(false);           // 取消返回键的作用(强制升级，一般要很少用，用下面的)
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });

        builder.setTitle("提示：");
        builder.setMessage(description);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 下载apk并替换安装
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(apkurl,
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileSafe" + getVersionName() + ".apk",
                            new AjaxCallBack<File>() {
                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                    installAPK(file);
                                }

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    float progress = current * 100 / count;
                                    tv_update_info.setText("下载进度：" + progress + "%");
                                }

                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                /**
                                 * 安装 apk
                                 */
                                private void installAPK(File f) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
                                    startActivity(intent);
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "没有找到存储卡，请先安装存储卡", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 进入主页面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        // 进入主页后关闭当前页
        finish();
    }

    /**
     * 检查升级
     */
    private void checkForUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(getString(R.string.serverurl));
//                    URL url = new URL("http://192.168.72.1:8080/update/updadeinfo.html");
                    // 联网
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    conn.setReadTimeout(3000);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if(code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readFromStream(is);
//                        Log.i(TAG, "联网ok" + result);
                        // JSON 解析
                        JSONObject obj = new JSONObject(result);
                        String version = (String) obj.get("version");
                        description = (String) obj.get("description");
                        apkurl = (String) obj.get("apkurl");
                        if (getVersionName().equals(version)) {
                            // 没有新版本
                            msg.what = ENTER_HOME;
                        } else {
                            // 弹出升级对话框
                            msg.what = SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = JSON_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    // 1000 ms 才跳转
                    if(endTime - startTime < STAY_TIME) {
                        try {
                            Thread.sleep(STAY_TIME - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    /**
     * 获得 APP 版本信息
     * @return
     */
    private String getVersionName() {
        // 管理手机的 APK
        PackageManager pm = getPackageManager();
        try {
            // 得到指定 APK 的功能清单文件
            PackageInfo pi = pm.getPackageInfo("com.example.zhli.mobilesafe", 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}
