package com.example.zhli.lottery.net;

import android.text.TextUtils;

import com.example.zhli.lottery.ConstantValue;
import com.example.zhli.lottery.GlobalParams;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by zhli on 2015/2/9.
 */
public class HttpClientUtil {
    private HttpClient client;

    private HttpPost post;

    public HttpClientUtil() {
        client = new DefaultHttpClient();
        // 判断是否需要设置代理信息
        if (!TextUtils.isEmpty(GlobalParams.PROXY)) {
            // 设置代理设置
            HttpHost host = new HttpHost(GlobalParams.PROXY, GlobalParams.PORT);
            client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, host);

        }
    }

    /**
     * 向指定的链接发送 xml 文件
     * @param uri
     * @param xml
     */
    public InputStream sendXml(String uri, String xml) {
        post = new HttpPost(uri);
        StringEntity entity = null;
        try {
            entity = new StringEntity(xml, ConstantValue.ENCODING);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
