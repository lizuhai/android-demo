package com.example.zhli.lottery.engine;

import android.util.Xml;

import com.example.zhli.lottery.ConstantValue;
import com.example.zhli.lottery.net.HttpClientUtil;
import com.example.zhli.lottery.net.protocal.Message;
import com.example.zhli.lottery.utils.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by zhli on 2015/2/9.
 */
public abstract class BaseEngine {
    public Message aa(String xml) {
        // 第二步和第三步

        // 二、发送 xml 到服务器，等待回复
        HttpClientUtil util = new HttpClientUtil();
        InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (is != null) {
            Message result = new Message();
            // 三、数据的校验（MD5 数据校验） timestamp + digest + body
            XmlPullParser parser = Xml.newPullParser();
            String name;
            try {
                parser.setInput(is, ConstantValue.ENCODING);
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equals("timestamp")) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if (name.equals("digest")) {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if (name.equals("body")) {
                                result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 还原原始数据：时间戳，密码，body
            DES des = new DES();
            String body = "<body>"
                    + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD)
                    + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue() + ConstantValue.AGENTER_PASSWORD + body;
            // 利用工具生成手机端 md5, 并于服务器端进行比较
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
                // 比对成功
                return result;
            }
        }
        return null;
    }
}
