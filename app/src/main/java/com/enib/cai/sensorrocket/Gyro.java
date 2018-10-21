package com.enib.cai.sensorrocket;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Gyro implements SensorEventListener {
    public float x = 0;
    public float y = 0;
    public float z = 0;
    private SensorManager mSensorManager;
    private Sensor mGyroscope;

    public Gyro(Context context){
        mSensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    protected void onResume() {
        if(mSensorManager!=null){
            mSensorManager.registerListener(this,mGyroscope,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
