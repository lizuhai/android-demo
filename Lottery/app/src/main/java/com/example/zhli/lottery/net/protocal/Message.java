package com.example.zhli.lottery.net.protocal;

import android.util.Xml;

import com.example.zhli.lottery.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 协议的封装
 * Created by zhli on 2015/2/5.
 */
public class Message {
    private Header header = new Header();
    private Body body = new Body();

    /**
     * 序列化请求
     */
    public void serializerMessage(XmlSerializer serializer) {
        /*
        <?xml version=”1.0” encoding=”utf-8”?>
        <message version="1.0">
            <header>
                <agenterid>889931</agenterid>
                <source>ivr</source>
                <compress>DES</compress>
                <messengerid>20131013101533000001</messengerid>
                <timestamp>20131013101533</timestamp>
                <digest>7ec8582632678032d25866bd4bce114f</digest>
                <transactiontype>12002</transactiontype>
                <username>13200000000</username>
            </header>
            <body>
                <elements>
                    <element>
                        <lotteryid>118</lotteryid>
                        <issues>1</issues>
                    </element>
                </elements>
            </body>
        </message>
        */
        try {
            serializer.startTag(null, "message");
            serializer.attribute(null, "version", "1.0");

            header.serializerHeader(serializer, body.getWholeBody());    // 获取完整的 body
//            body.serializerBody(serializer);
            serializer.startTag(null, "body");
            serializer.text(body.getBodyInsideDESInfo());
            serializer.endTag(null, "body");

            serializer.endTag(null, "message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求的 xml 文件
     * @return
     */
    public String getXml(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element is null");
        }
        // 设置请求标识，设置请求内容
        header.getTransactiontype().setTagValue(element.getTransationType());
        body.getElements().add(element);

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCODING, null);

            this.serializerMessage(serializer);
            serializer.endDocument();

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
