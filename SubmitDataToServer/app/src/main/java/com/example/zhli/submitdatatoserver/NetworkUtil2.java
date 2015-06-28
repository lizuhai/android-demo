package com.example.zhli.submitdatatoserver;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhli on 2015/1/21.
 */
public class NetworkUtil2 {
    private static final String TAG = "NetworkUtil2";

    /**
     * 使用 post 方式登陆
     * @param username
     * @param password
     * @return 登陆的状态
     */
    public static String loginOfGet(String username, String password) {
        // 定义一个 HttpClient
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            String data = "username=" + username + "&password=" + password;
            // 定义一个 get 请求方法
            HttpGet get = new HttpGet("http://192.168.72.1:8080/AndroidTest/loginServlet?" + data);
            // 开始执行 get 方法，请求网络
            HttpResponse response = client.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                InputStream is = response.getEntity().getContent();
                String text = getStringFromInputStream(is);
                return text;
            } else {
                Log.i(TAG, "请求失败" + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client != null)
                client.getConnectionManager().shutdown();   // 关闭连接，释放资源
        }
        return null;
    }


    /**
     * 使用 get 方式登陆
     * @param username
     * @param password
     * @return 登陆的状态
     */
    public static String loginOfPost(String username, String password) {
        HttpClient client = null;
        try {
            // 定义 HttpClient
            client = new DefaultHttpClient();
            // 定义 post 请求
            HttpPost post = new HttpPost("http://192.168.72.1:8080/AndroidTest/loginServlet");
                // 定义 post 请求的参数
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            // 设置参数
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200) {
                InputStream is = response.getEntity().getContent();
                return getStringFromInputStream(is);
            } else {
                Log.i(TAG, "请求失败" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client != null)
                client.getConnectionManager().shutdown();
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
