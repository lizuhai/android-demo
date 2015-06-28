package com.example.zhli.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private SensorManager sm;
    private MyListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 光线传感器
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);    // 采样频率（第三个参数）
    }

    private class MyListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float light = event.values[0];    //光线强弱
            System.out.println("light: " + light);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }

    @Override
    protected void onDestroy() {
        sm.unregisterListener(listener);
        listener = null;
        super.onDestroy();
    }
}
