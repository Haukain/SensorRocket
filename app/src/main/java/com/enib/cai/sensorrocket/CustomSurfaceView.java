package com.enib.cai.sensorrocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Rocket mRocket;
    private Vector<Ennemy> mEnnemy;
    private Thread mThread;
    private Gyro mGyro;
    private Lumen mLumen;
    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning;
    private int mbgColor;
    private boolean mRocketPosUnset;

    public CustomSurfaceView (Context context){
        super(context);
        mRocket = new Rocket();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mGyro = new Gyro(context);
        mLumen = new Lumen(context);
        mbgColor = Color.argb(255,135, 206, 235);
        mEnnemy = new Vector<Ennemy>();
        mRocketPosUnset = true;
    }

    public void onPause()
    {
        mGyro.onPause();
        mLumen.onPause();
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e)
        {

        }
    }

    public void onResume() {
        mGyro.onResume();
        mLumen.onResume();
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
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), bgPaint);

                mSurfaceHolder.unlockCanvasAndPost(canvas);

            // Setting the rocket position for the first time
            if(mRocketPosUnset)
            {
                if(this.getWidth()!=0) {
                    mRocket.setPosition(new Point(this.getWidth() / 2, this.getHeight() - 150));
                    mRocketPosUnset = false;
                }
            }
            // Making sure we don't run any loop while the rocket has no position
            else {

                // Random test to see if we should add an ennemy to the ennemy vector
                if (Math.random() < 0.01) {
                    mEnnemy.add(new Ennemy(this.getWidth(), (int) (100 * Math.random())));
                }

                // Drawing loop
                if (mSurfaceHolder.getSurface().isValid()) {
                    canvas = mSurfaceHolder.lockCanvas();

                    //Painting background
                    Paint bgPaint = new Paint();
                    bgPaint.setColor(mbgColor);
                    canvas.drawPaint(bgPaint);

                    //Drawing rocket
                    canvas.drawPath(mRocket.drawRocket(), mRocket.getPaint());

                    //Updating the rocket position
                    float gyroOffset = Math.abs(mGyro.y)>0.1?mGyro.y*20:0;
                    float newX = mRocket.getPosition().x + gyroOffset;
                    if (newX > this.getWidth()) newX = this.getWidth();
                    else if (newX < 0) newX = 0;
                    mRocket.setPosition(new Point((int) newX, mRocket.getPosition().y));
                    Log.d("new pos", String.valueOf(mRocket.getPosition().x) + "  " + String.valueOf(mRocket.getPosition().y));

                    //Iterating the ennemy vector
                    Iterator<Ennemy> it = mEnnemy.iterator();
                    while (it.hasNext()) {
                        Ennemy e = it.next();
                        //Drawing the ennemy
                        canvas.drawRect(e.getPosition().x, e.getPosition().y, e.getPosition().x + e.getSize(),
                                e.getPosition().y + e.getSize(), e.getPaint());
                        //Updating the ennemy position
                        e.setPosition(new Point(e.getPosition().x, e.getPosition().y + e.getSpeed()));
                        if (e.getPosition().y > (this.getHeight() + 10)) {
                            //removing ennemy if it is out of bounds
                            it.remove();
                        }
                    }

                    //Drawing gyroscope text
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(50);
                    canvas.drawText("Gyro X : " + String.format("%.1f", mGyro.x), 50, 100, textPaint);
                    canvas.drawText("Gyro Y : " + String.format("%.1f", mGyro.y), 50, 200, textPaint);
                    canvas.drawText("Gyro Z : " + String.format("%.1f", mGyro.z), 50, 300, textPaint);
                    canvas.drawText("Light : "+String.valueOf(mLumen.currentLux), 50, 400, textPaint);
                    canvas.drawText("MinLight : "+String.valueOf(mLumen.minLux), 50, 500, textPaint);
                    canvas.drawText("MaxLight : "+String.valueOf(mLumen.maxLux), 50, 600, textPaint);
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
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
