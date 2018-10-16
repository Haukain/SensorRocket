package com.enib.cai.sensorrocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Vector;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Rocket mRocket;
    private Vector<Ennemy> mEnnemy;
    private Thread mThread;
    private Gyro mGyro;
    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning;
    private int mbgColor;

    public CustomSurfaceView (Context context){
        super(context);
        mRocket = new Rocket();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mGyro = new Gyro(context);
        mbgColor = Color.argb(255,135, 206, 235);
    }

    public void onPause()
    {
        mGyro.onPause();
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e)
        {

        }
    }

    public void onResume() {
        mGyro.onResume();
        mRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void run() {
        Canvas canvas;
        while (mRunning) {
            if (mSurfaceHolder.getSurface().isValid()) {
                canvas = mSurfaceHolder.lockCanvas();

                Paint bgPaint = new Paint();
                bgPaint.setColor(mbgColor);
                canvas.drawPaint(bgPaint);

                canvas.drawPath(mRocket.drawRocket(this.getWidth(),this.getHeight(),mGyro.y*20),mRocket.getPaint());

                Paint textPaint = new Paint();
                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(50);
                canvas.drawText("Gyro X : "+String.format("%.1f",mGyro.x), 50, 100, textPaint);
                canvas.drawText("Gyro Y : "+String.format("%.1f",mGyro.y), 50, 200, textPaint);
                canvas.drawText("Gyro Z : "+String.format("%.1f",mGyro.z), 50, 300, textPaint);

                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
