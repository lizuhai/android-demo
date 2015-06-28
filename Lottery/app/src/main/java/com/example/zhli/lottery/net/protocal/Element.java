package com.example.zhli.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * 请求数据的封装
 * Created by zhli on 2015/2/4.
 */
public abstract class Element {
    // 公共部分：
    // (1) 序列化
    /**
     * 序列化请求
     */
    public abstract void serializerElement(XmlSerializer serializer);
    // (2) 标识
    /**
     * 获取请求的标识
     */
    public abstract String getTransationType();


    // 1. 包含的内容
    // 2. 序列化
    // 3. 特有的方法，获取请求的标识

    // <lotteryid>118</lotteryid>
    // <issues>1</issues>
//    private Leaf lotteryid = new Leaf("lotteryid");
//    private Leaf issues = new Leaf("issues", "1");

//    public Leaf getLotteryid() {
//        return lotteryid;
//    }


//    public void serializerElement(XmlSerializer serializer) {
//        try {
//            serializer.startTag(null, "element");
//            lotteryid.serializerLeaf(serializer);
//            issues.serializerLeaf(serializer);
//            serializer.endTag(null, "element");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    public String getTransationType() {
//        return "12002";
//    }
}
