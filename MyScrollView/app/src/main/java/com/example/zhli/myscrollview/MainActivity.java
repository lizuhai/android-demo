package com.example.zhli.myscrollview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    private MyScrollView msv;
    private int[] ids = {R.drawable.channel1, R.drawable.channel2,R.drawable.channel3,
            R.drawable.channel4,R.drawable.channel5,R.drawable.channel6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msv = (MyScrollView) findViewById(R.id.myscroll_view);
        for (int i = 0; i < ids.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(ids[i]);
            msv.addView(image);
        }
    }
}
