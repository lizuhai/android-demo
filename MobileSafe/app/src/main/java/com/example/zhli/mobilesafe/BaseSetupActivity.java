package com.example.zhli.mobilesafe;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public abstract class BaseSetupActivity extends ActionBarActivity {

    private GestureDetector detector;       // 手势识别器
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 滑动手机屏幕回掉
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                // 屏蔽滑动太慢的情况(在兜里。。。)
                if(Math.abs(velocityX) < 200) {
                    Toast.makeText(getApplicationContext(), "亲，滑动的太慢了", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(e2.getRawX() - e1.getRawX() > 150) {          // 上一页
                    showPre();
                    return true;    // 不用在做其他事情了
                }
                if(e1.getRawX() - e2.getRawX() > 150) {          // 下一页
                    showNext();
                    return true;    // 不用在做其他事情了
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void showNext();
    protected abstract void showPre();

    /**
     * 下一步的点击事件
     */
    public void next(View v) {
        showNext();
    }

    /**
     * 上一步的点击事件
     */
    public void pre(View v) {
        showPre();
    }

    /**
     * 使用手势识别器
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
