package com.example.zhli.login;

import android.content.Context;
import android.os.Environment;
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
public class UtilsOfSdcard {
    /**
     * 存储用户名和密码，成功返回 true
     * @param number
     * @param password
     * @return
     */
    public static boolean saveUserInfoToSDCard(String number, String password) {
        try {
            // 判断有没有 SDCard
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state))
                return false;
            // 获取 SDCard
            File sdCard = Environment.getExternalStorageDirectory();
            File f = new File(sdCard, "aa.txt");
            FileOutputStream fos = new FileOutputStream(f);
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

    /**
     *
     * @return 用户信息
     */
    public static Map<String, String> getUserInfoFromSDCard() {
        try {
            // 判断有没有 SDCard
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state))
                return null;
            File sdCard = Environment.getExternalStorageDirectory();
            File f = new File(sdCard, "aa.txt");
            FileInputStream fis = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String tmp = reader.readLine();
            reader.close();
            fis.close();
            if(tmp != null) {
                String[] split = tmp.split("##");
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("number", split[0]);
                userInfo.put("password", split[1]);
//                System.out.println(userInfo);
                return userInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
