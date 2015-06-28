package com.example.zhli.login;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

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
public class Utils {
    /**
     * 存储用户名和密码，成功返回 true
     * @param number
     * @param password
     * @return
     */
    public static boolean saveUserInfo(String number, String password) {
        try {
            String path = "/data/data/com.example.zhli.login/a.txt";
            FileOutputStream fos = new FileOutputStream(path);
            // 234##123321
            String data = number + "##" + password;
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean saveUserInfo(Context context, String number, String password) {
        try {
//            String path = "/data/data/com.example.zhli.login/a.txt";
            File fileDir = context.getFilesDir();
//            File fileDir = context.getCacheDir();     // 保存到缓存里面去
            Toast.makeText(context, fileDir.toString(), Toast.LENGTH_SHORT).show();
            File f = new File(fileDir, "a.txt");
            FileOutputStream fos = new FileOutputStream(f);
            // 234##123321
            String data = number + "##" + password;
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
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
    public static Map<String, String> getUserInfo() {
        try {
            String path = "/data/data/com.example.zhli.login/a.txt";
            FileInputStream fis = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String tmp = reader.readLine();
            System.out.println(tmp);
            if(tmp != null) {
                String[] split = tmp.split("##");
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("number", split[0]);
                userInfo.put("password", split[1]);
                return userInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> getUserInfo(Context context) {
        try {
//            String path = "/data/data/com.example.zhli.login/a.txt";
            File fileDir = context.getFilesDir();
            Toast.makeText(context, fileDir.toString(), Toast.LENGTH_SHORT).show();
            File f = new File(fileDir, "a.txt");
            FileInputStream fis = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String tmp = reader.readLine();
            if(tmp != null) {
                String[] split = tmp.split("##");
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("number", split[0]);
                userInfo.put("password", split[1]);
                Toast.makeText(context, split[0], Toast.LENGTH_SHORT).show();
                return userInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
