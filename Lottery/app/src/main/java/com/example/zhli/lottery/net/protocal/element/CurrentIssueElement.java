package com.example.zhli.lottery.net.protocal.element;

import com.example.zhli.lottery.net.protocal.Element;
import com.example.zhli.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

/**
 * 获取当前销售期的请求
 * Created by zhli on 2015/2/5.
 */
public class CurrentIssueElement extends Element {
    // <lotteryid>118</lotteryid>
    private Leaf lotteryid = new Leaf("lotteryid");
    // <issues>1</issues>
    private Leaf issues = new Leaf("issues", "1");

    /**
     * 序列化
     * @param serializer
     */
    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            lotteryid.serializerLeaf(serializer);
            issues.serializerLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Leaf getLotteryid() {
        return lotteryid;
    }

    /**
     * 获取标识
     * @return
     */
    @Override
    public String getTransationType() {
        return "12002";
    }
}
