package com.example.zhli.lottery.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.zhli.lottery.GlobalParams;

/**
 *
 * Created by zhli on 2015/2/9.
 */
public class NetUtil {

    /**
     * 检查用户的网络
     */
    public static boolean checkNet(Context context) {
        // 判段 wifi 连接了吗
        boolean isWifi = isWifiConnection(context);
        // 判段 Mobile 连接了吗
        boolean isMobile = isMobileConnection(context);

        if (isMobile) { // 若 Mobile 连接了
            readAPN(context);      // 判断哪个 apn 被选中了
            // 代理信息有内容 -->  wap 方式

        }
        if (!isMobile && !isWifi) {
            return false;
        }
        return true;
    }

    private static void readAPN(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri URL_PREFERAPN = Uri.parse("content://telephony/carriers/preferapn");    // 4.0 模拟器屏蔽掉该权限
        Cursor cursor = resolver.query(URL_PREFERAPN, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
            GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }
    }

    private static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    private static boolean isWifiConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
