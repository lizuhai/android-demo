package com.example.zhli.stepcount;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private SensorListener sensorListener;
    private int count;
    private TextView tv;
    private SensorManager sm;
    private MainActivity mainActivity;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        tv = (TextView) findViewById(R.id.tv);
        sensorListener = new SensorListener();

        registerSensor(Sensor.TYPE_STEP_DETECTOR);
    }


    private void registerSensor(int typeStepDetector) {
        sm = (SensorManager) getMainActivity().getSystemService(Activity.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(typeStepDetector);
        if (sensor != null) {
            tv.setText("support step detector sensor");
        } else {
            tv.setText("not support step detector sensor");
        }

        sm.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
//            System.out.println(event.values.length);
            count += event.values[0];
            System.out.println(count);
            if (tv != null) {
                tv.setText(count + "");
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    public void step(View v) {
        Intent intent = new Intent(this, StepActivity.class);
        startActivity(intent);
    }
}
