package com.example.zhli.viewanimation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
    }

    public void alpha(View v) {
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(2000);
        aa.setRepeatCount(3);       // 重复播放三次，共播放四次，-1（或者Animation.INIFINITE）表示永远不停的播放
        aa.setRepeatMode(Animation.REVERSE);    // 倒叙播放
        aa.setFillAfter(true);              //动画播放完成后停留，false表示播放完停留在开始
        iv.startAnimation(aa);
    }

    public void trans(View v) {
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f);

        ta.setDuration(2000);
        ta.setRepeatCount(1);
        ta.setRepeatMode(Animation.REVERSE);
        iv.startAnimation(ta);
    }

    public void scale(View v) {
        ScaleAnimation sa = new ScaleAnimation(0.1f, 2.0f, 0.1f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setRepeatCount(1);
        sa.setRepeatMode(Animation.REVERSE);
        iv.startAnimation(sa);
    }

    public void rotate(View v) {
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setRepeatCount(1);
        ra.setRepeatMode(Animation.REVERSE);
        iv.startAnimation(ra);
    }

    public void combine(View v) {
        AnimationSet set = new AnimationSet(false); // 各自用自己的播放速度
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f);

        ta.setDuration(2000);
        ta.setRepeatCount(1);
        ta.setRepeatMode(Animation.REVERSE);

        ScaleAnimation sa = new ScaleAnimation(0.1f, 2.0f, 0.1f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setRepeatCount(1);
        sa.setRepeatMode(Animation.REVERSE);

        RotateAnimation ra = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setRepeatCount(1);
        ra.setRepeatMode(Animation.REVERSE);

        set.addAnimation(ta);
        set.addAnimation(sa);
        set.addAnimation(ra);

        iv.startAnimation(set);
    }
}
