package com.enib.cai.sensorrocket;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Lumen implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mLight;
    int minLux = 40000;
    int currentLux = 0;
    int maxLux = 0;

    public Lumen(Context context)
    {
        mSensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    protected void onResume() {
        if(mSensorManager!=null){
            mSensorManager.registerListener(this,mLight,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        if(sensor.getType() == Sensor.TYPE_LIGHT)
        {

        }
    }

    public void onSensorChanged(SensorEvent event)
    {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            currentLux = (int)event.values[0];
            if (currentLux > maxLux)
                maxLux = currentLux;
            if (currentLux < minLux)
                minLux = currentLux;
        }
    }
}