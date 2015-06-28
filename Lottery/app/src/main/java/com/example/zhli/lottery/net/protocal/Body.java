package com.example.zhli.lottery.net.protocal;

import android.text.TextUtils;
import android.util.Xml;

import com.example.zhli.lottery.ConstantValue;
import com.example.zhli.lottery.bean.Oelement;
import com.example.zhli.lottery.utils.DES;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息体节点封装
 * Created by zhli on 2015/2/5.
 */
public class Body {
    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
    }

    /**
     * 序列化请求
     */
    public void serializerBody(XmlSerializer serializer) {
        //<body>
        //  <elements>
        //      <element>
        //          <lotteryid>118</lotteryid>
        //          <issues>1</issues>
        //      </element>
        //  </elements>
        //</body>
        try {
            serializer.startTag(null, "body");
            serializer.startTag(null, "elements");
            for (Element item : elements) {
                item.serializerElement(serializer);
            }
            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 只获取完整的 body
     * @return
     */
    public String getWholeBody() {
        XmlSerializer temp = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            temp.setOutput(writer);
            serializerBody(temp);
            // output will be flushed
            temp.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 body 里面的 DES 加密数据
     */
    public String getBodyInsideDESInfo() {
        // 加密数据
        String wholeBody = getWholeBody();
//        String orgDES = StringUtils.substringBetween(wholeBody, "<body>", "</body>");
        String orgDESInfo = TextUtils.substring(wholeBody, wholeBody.indexOf("<body>") + "<body>".length(), wholeBody.indexOf("</body>"));
        // 加密
        DES des = new DES();
        return des.authcode(orgDESInfo, "DECODE", ConstantValue.DES_PASSWORD);
    }


    /************************下面是处理服务器返回信息的*************************/
    private String serviceBodyInsideDESInfo;  // 服务器端回复的 body 中 DES 加密的信息
    private Oelement oelement = new Oelement();

    public String getServiceBodyInsideDESInfo() {
        return serviceBodyInsideDESInfo;
    }
    public void setServiceBodyInsideDESInfo(String serviceBodyInsideDESInfo) {
        this.serviceBodyInsideDESInfo = serviceBodyInsideDESInfo;
    }

    public Oelement getOelement() {
        return oelement;
    }
    /************************下面是处理服务器返回信息的*************************/
}
