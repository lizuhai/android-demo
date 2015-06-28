package com.example.zhli.lottery.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 淡入淡出的切换动画
 * Created by zhli on 2015/2/10.
 */
public class FadeUtil {
    // 当前正在展示的淡出，动画的执行时间
    // 在这个执行过程中，第二个节目处于等待状态
    // 第二个界面淡入，动画的执行时间

    /**
     * 淡出
     * @param view 执行动画的界面
     * @param duration 执行的时间
     */
    public static void fadeOut(View view, long duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);

        // 动画执行完成后，删除 view
        // 增加动画执行完成之后的监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(alphaAnimation);
    }

    /**
     * 淡入
     * @param view 执行动画的界面
     * @param delay 等待的时间，一般和淡出的执行时间一样
     * @param duration 执行的时间
     */
    public static void fadeIn(View view, long delay, long duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setStartOffset(delay);   // 设置延时的时间
        alphaAnimation.setDuration(duration);
        view.startAnimation(alphaAnimation);
    }
}
