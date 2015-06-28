package com.example.zhli.stepcount;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zhli on 2015/6/21.
 */
public class Pedometer implements SensorEventListener {

    private Context context;                // context
    private SensorManager mSensorManager;   // sensor manager
    private Sensor mStepCounterSensor;      // step counter sensor
    private Sensor mStepDectorSensor;       // step dector sensor
    private float mCount;                   // total step number
    private float mDetector;                  // step dector

    public Pedometer() {}

    public Pedometer(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public void register() {
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            setmCount(sensorEvent.values[0]);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sensorEvent.values[0] == 1.0) {
                mDetector += 1;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getmCount() {
        return mCount;
    }

    public void setmCount(float mCount) {
        this.mCount = mCount;
    }

    public float getmDetector() {
        return mDetector;
    }

    public void unRegister() {
        mSensorManager.unregisterListener(this);
    }
}
