package com.example.zhli.xmlanimation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
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
        Animation aa = AnimationUtils.loadAnimation(this, R.anim.alpha);
        iv.startAnimation(aa);
    }

    public void trans(View v) {
        Animation aa = AnimationUtils.loadAnimation(this, R.anim.trans);
        iv.startAnimation(aa);
    }

    public void scale(View v) {
        Animation aa = AnimationUtils.loadAnimation(this, R.anim.scale);
        iv.startAnimation(aa);
    }

    public void rotate(View v) {
        Animation aa = AnimationUtils.loadAnimation(this, R.anim.rotate);
        iv.startAnimation(aa);
    }

    public void set(View v) {
        Animation set = AnimationUtils.loadAnimation(this, R.anim.set);
        iv.startAnimation(set);
    }
}
