package com.example.zhli.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhli on 2015/1/26.
 * 标准 md5 加密算法
 */
public class MD5Utils {
    public static String md5(String s) {
        StringBuffer sb = new StringBuffer();
        try {
            // 1. 得到一个字节数组
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(s.getBytes());
            for (byte b : result) {
                // 2. 将每个字节与 0xff (1111 1111)做与运算
                int number = b & 0xff;
                // 3. 将得到的转成16进制，不足两位的（在前面加0）补足两位
                String str = Integer.toHexString(number);
                if(str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        return String.valueOf(sb);
    }
}
