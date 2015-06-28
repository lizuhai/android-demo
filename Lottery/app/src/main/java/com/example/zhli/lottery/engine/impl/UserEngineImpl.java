package com.example.zhli.lottery.engine.impl;

import android.util.Xml;

import com.example.zhli.lottery.ConstantValue;
import com.example.zhli.lottery.bean.User;
import com.example.zhli.lottery.engine.BaseEngine;
import com.example.zhli.lottery.engine.UserEngine;
import com.example.zhli.lottery.net.HttpClientUtil;
import com.example.zhli.lottery.net.protocal.Message;
import com.example.zhli.lottery.net.protocal.element.UserLoginElement;
import com.example.zhli.lottery.utils.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by zhli on 2015/2/9.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine {

    /**
     * 用户登录
     * @param user
     */
    public Message login(User user) {
        // 一、获取到登陆的 xml
        // 1. 新建用户登录的 Element
        UserLoginElement element = new UserLoginElement();
        // 2. 设置登录用的数据
        element.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);
        System.out.println("xml " + xml);

        // 第二、三步，比对通过返回 result， 否则返回 null
        Message result = aa(xml);
        if (result != null) {
            // 四、 请求结果的处理
            XmlPullParser parser = Xml.newPullParser();
            try {
                // 还原原始数据：时间戳，密码，body
                DES des = new DES();
                String body = "<body>"
                        + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD)
                        + "</body>";
                parser.setInput(new StringReader(body));
                int eventType = parser.getEventType();
                String name;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equals("errorcode")) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if (name.equals("errormsg")) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

   /* public Message login1(User user) {
        // 一、获取到登陆的 xml
        // 1. 新建用户登录的 Element
        UserLoginElement element = new UserLoginElement();
        // 2. 设置登录用的数据
        element.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);
        System.out.println("xml " + xml);

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
                // 四、 请求结果的处理
                parser = Xml.newPullParser();
                try {
                    parser.setInput(new StringReader(body));
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                name = parser.getName();
                                if (name.equals("errorcode")) {
                                    result.getBody().getOelement().setErrorcode(parser.nextText());
                                }
                                if (name.equals("errormsg")) {
                                    result.getBody().getOelement().setErrormsg(parser.nextText());
                                }
                                break;
                        }
                        eventType = parser.next();
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }*/
}
