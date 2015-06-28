package com.example.zhli.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * xml 简单叶子
 * Created by zhli on 2015/2/4.
 */
public class Leaf {
    // 1. 序列化包含的内容
    // 2. 序列化 xml
    // <agenterid>889931</agenterid>
    private String tagName;
    private String tagValue;

    // 每个叶子必须有标签名
    public Leaf(String tagName) {
        this.tagName = tagName;
    }

    // 处理常量
    public Leaf(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * 序列化叶子
     * @param serializer XmlSerializer
     */
    public void serializerLeaf(XmlSerializer serializer) {
        try {
            serializer.startTag(null, tagName);
            if (tagValue == null) {
                tagValue = "";
            }
            serializer.text(tagValue);
            serializer.endTag(null, tagName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
