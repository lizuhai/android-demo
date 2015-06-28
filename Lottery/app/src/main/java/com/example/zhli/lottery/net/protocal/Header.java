package com.example.zhli.lottery.net.protocal;

import com.example.zhli.lottery.ConstantValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 头节点的封装
 * Created by zhli on 2015/2/4.
 */
public class Header {

    //<agenterid>889931</agenterid>
    private Leaf agenterid = new Leaf("agenterid", ConstantValue.AGENTID);
    //<source>ivr</source>
    private Leaf source = new Leaf("source", ConstantValue.SOURCE);
    //<compress>DES</compress>
    private Leaf compress = new Leaf("compress", ConstantValue.COMPRESS);
    //<messengerid>20131013101533000001</messengerid>
    private Leaf messengerid = new Leaf("messengerid");
    //<timestamp>20131013101533</timestamp>
    private Leaf timestamp = new Leaf("timestamp");
    //<digest>7ec8582632678032d25866bd4bce114f</digest>
    private Leaf digest = new Leaf("digest");
    //<transactiontype>12002</transactiontype>
    private Leaf transactiontype = new Leaf("transactiontype");
    //<username>13200000000</username>
    private Leaf username = new Leaf("username");

    /**
     * 序列化头
     * @param serializer
     * @param body 完整的明文
     */
    public void serializerHeader(XmlSerializer serializer, String body) {
        try {

            // 将 timestamp, messengerid, digest 设置数据
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = format.format(new Date());
            timestamp.setTagValue(time);

            // messengerid: timestamp + 6 位随机数
            Random random = new Random();
            int num = random.nextInt(999999) + 1;   // [1,999999]
            DecimalFormat decimalFormat = new DecimalFormat("000000");
            messengerid.setTagValue(time + decimalFormat.format(num));

            // digest: timestamp + 代理商的秘密 + 完整的 body
            String orgInfo = time + ConstantValue.AGENTER_PASSWORD + body;
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            digest.setTagValue(md5Hex);

            serializer.startTag(null, "header");
            agenterid.serializerLeaf(serializer);
            source.serializerLeaf(serializer);
            compress.serializerLeaf(serializer);
            messengerid.serializerLeaf(serializer);

            timestamp.serializerLeaf(serializer);
            digest.serializerLeaf(serializer);
            transactiontype.serializerLeaf(serializer);
            username.serializerLeaf(serializer);

            serializer.endTag(null, "header");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Leaf getTransactiontype() {
        return transactiontype;
    }

    public Leaf getUsername() {
        return username;
    }


    /************************下面是处理服务器返回信息的*************************/
    public Leaf getDigest() {
        return digest;
    }

    public Leaf getTimestamp() {
        return timestamp;
    }
}
