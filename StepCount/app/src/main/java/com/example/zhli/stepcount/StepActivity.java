package com.example.zhli.stepcount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class StepActivity extends ActionBarActivity {

    private Pedometer pedometer;
    private TextView mCounter;
    StringBuilder mBuilder = new StringBuilder();
    private TextView mDetector;
    private ViewPropertyAnimator mStepEventAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        pedometer = new Pedometer(this);
        mCounter = (TextView) findViewById(R.id.counter_1);
        mDetector = (TextView) findViewById(R.id.detector_1);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mCounter.post(new Runnable() {
                    @Override
                    public void run() {
                        mCounter.setText("" + pedometer.getmCount());
                        mCounter.postInvalidate();
                    }
                });

                mDetector.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mStepEventAnim != null) {
                            mStepEventAnim.cancel();
                        }
                        mDetector.setText("" + pedometer.getmDetector());
                        mDetector.postInvalidate();
                        mDetector.setAlpha(1f);
                        mStepEventAnim = mDetector.animate().setDuration(500).alpha(0f);
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 100, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pedometer.register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pedometer.unRegister();
    }
}
