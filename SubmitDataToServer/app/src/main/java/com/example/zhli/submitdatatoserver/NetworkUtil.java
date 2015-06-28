package com.example.zhli.submitdatatoserver;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zhli on 2015/1/21.
 */
public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    /**
     * 使用 post 方式登陆
     * @param username
     * @param password
     * @return 登陆的状态
     */
    public static String loginOfPost(String username, String password) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://192.168.72.1:8080/AndroidTest/loginServlet");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);

            // 设置服务器允许输出（4.0 以上默认允许，低版本可能不允许）
            conn.setDoOutput(true);

//            conn.setRequestProperty("Content-Length", 123);  // 设定请求头消息

            // post 请求的参数
            String data = "username=" + username + "&password=" + password;
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int code = conn.getResponseCode();
            if(code == 200) {
                InputStream is = conn.getInputStream();
                String result = getStringFromInputStream(is);
                return result;
            } else {
                Log.i(TAG, "access failed" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }


    /**
     * 使用 get 方式登陆
     * @param username
     * @param password
     * @return 登陆的状态
     */
    public static String loginOfGet(String username, String password) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://192.168.72.1:8080/AndroidTest/loginServlet?username="
                    + URLEncoder.encode(username, "utf-8") + "&password=" + URLEncoder.encode(password, "utf-8"));

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            int code = conn.getResponseCode();
            if(code == 200) {
                InputStream is = conn.getInputStream();
                String result = getStringFromInputStream(is);
                return result;
            } else {
                Log.i(TAG, "access failed" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 根据流返回一个字符串信息
     * @return
     */
    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String html = new String(baos.toByteArray(), "UTF-8");
        baos.close();
        return html;
    }

}
