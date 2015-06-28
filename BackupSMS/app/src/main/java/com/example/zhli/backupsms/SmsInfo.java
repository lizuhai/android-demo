package com.example.zhli.backupsms;

/**
 * Created by zhli on 2015/1/20.
 */
public class SmsInfo {
    private int id;
    private String address;
    private long date;
    private int type;   // 1: 接收，2：发送
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SmsInfo{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", body='" + body + '\'' +
                '}';
    }
}
