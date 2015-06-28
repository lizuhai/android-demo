package com.example.zhli.youkumenuapplication.utils;

import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by zhli on 2015/2/2.
 */
public class Animations {

    /**
     * 让指定的 view 旋转进入（顺时针180 - 360度）
     */
    public static void startAnimIn(RelativeLayout view, int i) {
        RotateAnimation ra = new RotateAnimation(180, 360, view.getWidth() / 2, view.getHeight());
        ra.setDuration(500);
        ra.setFillAfter(true);      // 动画执行完成后执行最后的状态
        ra.setStartOffset(i);
        view.startAnimation(ra);
    }

    /**
     * 让指定的 view 旋转进入（顺时针180 - 360度）,延时 i 毫秒
     */
    public static void startAnimOut(RelativeLayout view, int i) {
        RotateAnimation ra = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight());
        ra.setDuration(500);
        ra.setFillAfter(true);      // 动画执行完成后执行最后的状态
        ra.setStartOffset(i);
        view.startAnimation(ra);
    }
}
