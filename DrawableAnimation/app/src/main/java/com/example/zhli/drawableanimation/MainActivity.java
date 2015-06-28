package com.example.zhli.drawableanimation;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private ImageView iv;
    private AnimationDrawable mAnimationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        // 把 xml 文件设置为动画的背景
        iv.setBackgroundResource(R.drawable.girl);
        // 或取设置的动画资源。异步执行，可能要一段时间才能执行，所以把他放在触摸事件中
        mAnimationDrawable = (AnimationDrawable) iv.getBackground();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mAnimationDrawable.start();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
