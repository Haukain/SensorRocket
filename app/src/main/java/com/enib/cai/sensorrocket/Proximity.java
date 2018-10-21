package com.enib.cai.sensorrocket;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Proximity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private float distance;

    public Proximity(Context context)
    {
        mSensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    protected void onResume() {
        if(mSensorManager!=null){
            mSensorManager.registerListener(this,mProximity,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        if(sensor.getType() == Sensor.TYPE_PROXIMITY)
        {

        }
    }

    public void onSensorChanged(SensorEvent event)
    {
        if( event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            distance = event.values[0];

        }
    }

    public float getDistance() {
        return distance;
    }
}
