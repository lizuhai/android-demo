package com.example.zhli.myscrollview;

import android.content.Context;
import android.os.SystemClock;

/**
 * 计算位移距离的工具类
 * Created by zhli on 2015/2/3.
 */
public class MyScroller {

    private int startX;
    private int startY;
    private int distanceX;
    private int distanceY;
    private long startTime;     // 开始执行动画的时间
    private boolean isFinish;   // true:结束
    private int duration = 500;     // 默认运行的时间值
    private long currX;         //当前 X 值
    private long currY;         //当前 Y 值

    public MyScroller(Context ctx) {
    }
    public long getCurrX() {
        return currX;
    }

    public void setCurrX(long currX) {
        this.currX = currX;
    }

    /**
     *
     * @param startX 开始x坐标
     * @param startY
     * @param distanceX x方向要移动的距离
     * @param distanceY
     */
    public void startScroll(int startX, int startY, int distanceX, int distanceY) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;

        this.startTime = SystemClock.uptimeMillis();
        this.isFinish = false;
    }

    /**
     * 计算当前运行状况
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }
        long passTime = SystemClock.uptimeMillis() - startTime;
        if (passTime < duration) {
            currX = startX + distanceX * passTime / duration;
            currY = startY + distanceY * passTime / duration;
        }else{
            currX = startX + distanceX;
            currY = startY + distanceY;
            isFinish = true;
        }
        return true;
    }
}
