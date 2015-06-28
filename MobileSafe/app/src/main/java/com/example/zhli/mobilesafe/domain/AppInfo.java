package com.example.zhli.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息业务 bean
 * Created by zhli on 2015/1/30.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String packname;
    private boolean inRom;  // 安装在内存还是存储卡
    private boolean userApp;    // 用户用还是系统用

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", packname='" + packname + '\'' +
                ", inRom=" + inRom +
                ", userApp=" + userApp +
                '}';
    }
}
