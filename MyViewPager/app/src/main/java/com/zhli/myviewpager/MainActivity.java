package com.zhli.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private MyScrollView msv;
    private int[] ids = {R.drawable.channel1, R.drawable.channel2,R.drawable.channel3,
            R.drawable.channel4,R.drawable.channel5,R.drawable.channel6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msv = (MyScrollView) findViewById(R.id.msv);
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(ids[i]);
            msv.addView(iv);
        }

    }



}
