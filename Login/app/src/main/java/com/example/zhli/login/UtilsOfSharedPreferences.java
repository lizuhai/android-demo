package com.example.zhli.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhli on 2015/1/19.
 */
public class UtilsOfSharedPreferences {
    /**
     * 存储用户名和密码，成功返回 true
     * @param number
     * @param password
     * @return
     */
    public static boolean saveUserInfo(Context context, String number, String password) {
        try {
            // /data/data/包名/shared_prefs/zhli.xml
            SharedPreferences sp = context.getSharedPreferences("zhli", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("number", number);
            edit.putString("password", password);
            // 提交，从内存中保存到文件中
            edit.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return 用户信息
     */
    public static Map<String, String> getUserInfo(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences("zhli", Context.MODE_PRIVATE);
            String number = sp.getString("number", null);
            String password = sp.getString("password", null);
            if(number != null && password != null) {
                Map<String, String> userInfoMap = new HashMap<>();
                userInfoMap.put("number", number);
                userInfoMap.put("password", password);
                return userInfoMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
