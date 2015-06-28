package lockscreen.com.example.zhli.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private DevicePolicyManager dpm;        // 设备管理员(也是一个服务——设备策略服务)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
//        dpm.lockNow();
//        finish();
    }

    /**
     * 一键锁屏
     */
    public void lockscreen(View v) {
        ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
        if(dpm.isAdminActive(mDeviceAdminSample)) {
            dpm.lockNow();
            dpm.resetPassword("111", 0);     // 设置屏幕密码

//            finish();
        } else {
            Toast.makeText(this, "请先开启管理员权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void openAdmin(View v) {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 我要激活的东西
        ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        // 向用户解说用户开启管理员权限
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启即可一键锁屏");
        startActivity(intent);
    }

    /**
     * 清除 sdcard 上的数据
     */
    public void wipedata(View v) {
//        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
//        dpm.wipeData(0);        // 恢复出厂设置

    }

    /**
     * 卸载有管理员权限的软件
     */
    public void uninstall(View v) {
        // 先清除管理员权限
        ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
        dpm.removeActiveAdmin(mDeviceAdminSample);
        // 普通应用的卸载
        Intent intent = new Intent();
//        <activity android:name=".UninstallerActivity"
//        android:configChanges="orientation|keyboardHidden"
//        android:theme="@style/TallTitleBarTheme">
//        <intent-filter>
//        <action android:name="android.intent.action.VIEW" />
//        <action android:name="android.intent.action.DELETE" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <data android:scheme="package" />
//        </intent-filter>
//        </activity>
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
