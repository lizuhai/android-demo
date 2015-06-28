package com.example.zhli.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 *
 * Created by zhli on 2015/2/1.
 */
public class FileMd5 {
    /**
     * 计算文件的 md5 值
     */
    public static String md5Values(String filePath) {
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(filePath);
            // 1. 得到一个字节数组
            MessageDigest digest = MessageDigest.getInstance("md5");
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            fis.close();
            byte[] result = digest.digest();
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return String.valueOf(sb);
    }
}
