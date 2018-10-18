package com.enib.cai.sensorrocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private long mBeginningTime;
    private long mCurrentTime;
    private Rocket mRocket;
    private Vector<Ennemy> mEnnemy;
    private Thread mThread;
    private Accelero mAccelero;
    //private Gyro mGyro;
    private Lumen mLumen;
    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning;
    private int mbgColor;
    private boolean mRocketPosUnset;

    public CustomSurfaceView (Context context){
        super(context);

        mBeginningTime = System.currentTimeMillis();
        mCurrentTime = mBeginningTime;

        mRocket = new Rocket();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mAccelero = new Accelero(context);
        //mGyro = new Gyro(context);
        mLumen = new Lumen(context);
        mbgColor = Color.argb(255,135, 206, 235);
        mEnnemy = new Vector<Ennemy>();
        mRocketPosUnset = true;
    }

    public void onPause()
    {
        mAccelero.onPause();
        //mGyro.onPause();
        mLumen.onPause();
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e)
        {

        }
    }

    public void onResume() {
        mAccelero.onResume();
        //mGyro.onResume();
        mLumen.onResume();
        mRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void run() {
        Canvas canvas;
        while (mRunning) {

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
                if (Math.random() < 0.05 && mEnnemy.size()<30) {
                    mEnnemy.add(new Ennemy(this.getWidth()));
                }

                // Drawing loop
                if (mSurfaceHolder.getSurface().isValid()) {
                    canvas = mSurfaceHolder.lockCanvas();

                    //Painting background
                    Paint bgPaint = new Paint();
                    bgPaint.setColor(mbgColor);
                    canvas.drawPaint(bgPaint);

                    //Getting Rocket Path
                    Path rocketPath = mRocket.drawRocket();
                    //Getting Rocket Hitbox
                    Region rocketHitBox = new Region();
                    rocketHitBox.setPath(rocketPath,new Region(0,0,this.getWidth(),this.getHeight()));
                    //Drawing Rocket
                    canvas.drawPath(rocketPath, mRocket.getPaint());

                    //Updating the rocket position
                    float acceleroOffset = Math.abs(mAccelero.x)>0.1?(-mAccelero.x*2):0;
                    float newX = mRocket.getPosition().x + acceleroOffset;
                    if (newX > this.getWidth()) newX = this.getWidth();
                    else if (newX < 0) newX = 0;
                    mRocket.setPosition(new Point((int) newX, mRocket.getPosition().y));

                    boolean hitThisLoop = false;

                    //Iterating the ennemy vector
                    Iterator<Ennemy> it = mEnnemy.iterator();
                    while (it.hasNext()) {
                        Ennemy e = it.next();

                        //Getting Ennemy Path
                        Path ennemyPath = e.drawEnnemy();
                        //Getting Ennemy Hitbox
                        Region ennemyHitBox = new Region();
                        ennemyHitBox.setPath(ennemyPath,new Region(0,0,this.getWidth(),this.getHeight()));

                        if(!hitThisLoop) {
                            //Checking for collision only if not hit this loop
                            if (!rocketHitBox.quickReject(ennemyHitBox) && rocketHitBox.op(ennemyHitBox, Region.Op.INTERSECT)) {
                                //If not alread hit and currently hit, set "hit" to 1
                                if (!mRocket.getHit()) {
                                    hitThisLoop=true;
                                    mRocket.setHit(true);
                                }
                            }
                        }

                        //Drawing the Ennemy
                        canvas.drawPath(ennemyPath,e.getPaint());

                        //Updating the ennemy position
                        e.setPosition(new Point(e.getPosition().x, e.getPosition().y + e.getSpeed()));
                        if (e.getPosition().y > (this.getHeight() + 10)) {
                            //removing ennemy if it is out of bounds
                            it.remove();
                        }
                    }

                    if(!hitThisLoop && mRocket.getHit()) // If not hit by any ennemy and already hit, set "hit" to 0
                    {
                        mRocket.setHit(false);
                    }

                    //Drawing gyroscope text
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(50);
                    canvas.drawText("Acceleration X : " + String.format("%.1f", mAccelero.x), 50, 100, textPaint);
                    canvas.drawText("Acceleration Y : " + String.format("%.1f", mAccelero.y), 50, 200, textPaint);
                    canvas.drawText("Acceleration Z : " + String.format("%.1f", mAccelero.z), 50, 300, textPaint);
                    canvas.drawText("Light : "+String.valueOf(mLumen.currentLux), 50, 400, textPaint);
                    canvas.drawText("MinLight : "+String.valueOf(mLumen.minLux), 50, 500, textPaint);
                    canvas.drawText("MaxLight : "+String.valueOf(mLumen.maxLux), 50, 600, textPaint);

                    mCurrentTime = System.currentTimeMillis() - mBeginningTime;
                    canvas.drawText("Time : " + String.valueOf((int)(mCurrentTime/1000)),500,100,textPaint);

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
