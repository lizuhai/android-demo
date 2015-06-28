package com.example.zhli.lottery;

/**
 * Contant
 * Created by zhli on 2015/2/4.
 */
public interface ConstantValue {
    String ENCODING = "UTF-8";
    String AGENTID = "889931";      // 代理的 id
    String SOURCE = "ivr";          // 信息的来源：安卓手机
    String COMPRESS = "DES";        // xml 协议 body 里面的加密算法
    String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";   // 自代理商的密钥(.so) JNI
    String DES_PASSWORD = "9b2648fcdfbad80f";   // DES 加密密钥
    String LOTTERY_URI = "http://192.168.72.1:8080/ZCWService/Entrance";    // 服务器地址
}
