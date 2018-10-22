package com.enib.cai.sensorrocket;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.Vector;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    // Listener
    GameListener mListener;

    // Thread related
    private Thread mThread;
    private boolean mRunning;

    // Time
    private long mBeginningTime;
    private long mCurrentTime;
    private int mCurrentTimeInSecond;
    private long mTimeInPause;
    private long mPrevEventTime;
    private long mProxEventTime;

    // Score
    private int mScore;

    // Ennemy spawning
    private int mEnnemySpawnCountdown;
    private long mlastEnnemySpawn;

    // Scene Elements
    private Rocket mRocket;
    private Vector<Point> mRocketSmoke;
    private int mRocketExplosion;
    private Vector<Ennemy> mEnnemy;
    private int mbgColor;
    private boolean mRocketPosUnset;

    // On touch hitbox
    private Region mTouchHitBox;

    // Sensors
    private Accelero mAccelero;
    //private Gyro mGyro;
    private Lumen mLumen;
    private Proximity mProximity;

    // Vibrator
    private Vibrator mVibrator;

    // Holder for canvas
    private SurfaceHolder mSurfaceHolder;

    //Pause Flag
    private boolean mPaused;

    // End flag
    private boolean mEnded;

    public CustomSurfaceView (Context context,GameListener listener){
        super(context);
        mListener = listener;

        mAccelero = new Accelero(context);
        //mGyro = new Gyro(context);
        mLumen = new Lumen(context);
        mProximity = new Proximity(context);

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        init();
    }

    public void init()
    {
        mEnded = false;
        mScore = 0;

        mPaused = false;
        mTimeInPause = 0;

        mBeginningTime = System.currentTimeMillis();
        mCurrentTime = 0;
        mPrevEventTime = 0;
        mProxEventTime = 0;
        mEnnemySpawnCountdown=2000;
        mlastEnnemySpawn=0;

        mRocket = new Rocket();
        mRocketSmoke = new Vector<>();
        mRocketExplosion = 30;
        mEnnemy = new Vector<>();
        mbgColor = Color.argb(255,18, 62, 135);
        mRocketPosUnset = true;

        mTouchHitBox = new Region();
    }

    // Activity onPause
    public void onPause()
    {
        mAccelero.onPause();
        //mGyro.onPause();
        mLumen.onPause();
        mProximity.onPause();
        mRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e)
        {
            // catch error
        }
    }

    // Activity onResume
    public void onResume() {
        mAccelero.onResume();
        //mGyro.onResume();
        mLumen.onResume();
        mProximity.onResume();
        mRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    // Thread running loop
    @Override
    public void run() {
        Canvas canvas;

        while (mRunning) {

            mTouchHitBox = new Region();
            mbgColor = Color.argb(255,mLumen.currentLux*30/30000,mLumen.currentLux*100/30000, mLumen.currentLux*255/30000);

            if(!mPaused && !mEnded) {
                // Setting the rocket position for the first time
                if (mRocketPosUnset) {
                    if (this.getWidth() != 0) {
                        mRocket.setPosition(new Point(this.getWidth() / 2, this.getHeight() - 250));
                        mRocketPosUnset = false;
                    }
                }
                // Making sure we don't run any loop while the rocket has no position
                else {
                    // Drawing loop
                    if (mSurfaceHolder.getSurface().isValid()) {
                        canvas = mSurfaceHolder.lockCanvas();

                        // Updating Time
                        mCurrentTime = System.currentTimeMillis() - mBeginningTime - mTimeInPause;
                        mCurrentTimeInSecond = (int) mCurrentTime / 1000;
                        if(mRocket.isAlive()) mScore = mCurrentTimeInSecond;

                        // Testing if ennemy should appear
                        if ((mCurrentTime - mlastEnnemySpawn) > (mEnnemySpawnCountdown / (1 + mCurrentTimeInSecond / 10))) {
                            mEnnemy.add(new Ennemy(this.getWidth()));
                            mlastEnnemySpawn = mCurrentTime;
                        }

                        //Painting background
                        canvas.drawColor(mbgColor);

                        Region rocketHitBox = new Region();
                        if(mRocket.isAlive()) {
                            //Drawing animation
                            Paint smokePaint = new Paint();
                            smokePaint.setColor(Color.argb((int) (Math.random() * 50 + 50), 255, 255, 255));
                            if (mRocketSmoke.size() < 20)
                                mRocketSmoke.add(new Point(mRocket.getPosition().x, mRocket.getPosition().y + 30));
                            Iterator<Point> pi = mRocketSmoke.iterator();
                            while (pi.hasNext()) {
                                Point p = pi.next();
                                canvas.drawCircle(p.x, p.y, (int) (Math.random() * 25 + 20), smokePaint);
                                if (p.y > this.getHeight() + 50) pi.remove();
                                else p.set(p.x + (int) (Math.random() * 10 - 5), p.y + 20);
                            }

                            //Getting Rocket Path
                            Path rocketPath = mRocket.drawRocket();
                            //Getting Rocket Hitbox
                            rocketHitBox.setPath(rocketPath, new Region(0, 0, this.getWidth(), this.getHeight()));
                            //Drawing Rocket
                            canvas.drawPath(rocketPath, mRocket.getPaint());
                        }
                        else{

                            //Drawing explosion animation
                            Paint explosionPaint = new Paint();
                            for(int i=0;i<mRocketExplosion;i++)
                            {
                                explosionPaint.setColor(Color.argb((int) (Math.random() * 50 + 50), 255, (int) (Math.random() * 255), 0));
                                canvas.drawCircle(mRocket.getPosition().x, mRocket.getPosition().y, (int) (Math.random() * 100 + 100), explosionPaint);
                            }
                            mRocketExplosion--;
                            if(mRocketExplosion<0) mEnded=true;
                        }

                        //Updating the rocket position
                        float acceleroOffset = Math.abs(mAccelero.x) > 0.2 ? (-mAccelero.x * 3) : 0;
                        float newX = mRocket.getPosition().x + acceleroOffset;
                        if (newX > this.getWidth()) newX = this.getWidth();
                        else if (newX < 0) newX = 0;
                        mRocket.setPosition(new Point((int) newX, mRocket.getPosition().y));

                        boolean hitThisLoop = false;

                        //Time before next TouchEvent
                        Paint waitingPaint = new Paint();
                        waitingPaint.setColor(Color.WHITE);
                        long diff = mCurrentTime-mPrevEventTime;
                        if (diff>=3000)
                        {
                            waitingPaint.setColor(Color.GREEN);
                            canvas.drawCircle(this.getWidth()-150 , this.getHeight()-100,50,waitingPaint);
                            waitingPaint.setColor(Color.WHITE);
                        }
                        else
                        {
                            canvas.drawArc(this.getWidth()-200 , this.getHeight()-150,this.getWidth()-100,this.getHeight()-50,270-(float)diff/3000*360,(float)diff/3000*360,true,waitingPaint);
                        }

                        //Time before next ProximityEvent
                        long dif = mCurrentTime-mProxEventTime;
                        if (dif>=10000)
                        {
                            waitingPaint.setColor(Color.GREEN);
                            canvas.drawCircle(this.getWidth()-150 , this.getHeight()-300,50,waitingPaint);
                        }
                        else
                        {
                            canvas.drawArc(this.getWidth()-200 , this.getHeight()-350,this.getWidth()-100,this.getHeight()-250,270-(float)dif/10000*360,(float)dif/10000*360,true,waitingPaint);
                        }
                        boolean prox = false;
                        if ((mProximity.getDistance() == 0)&(dif>=10000)) {
                            mProxEventTime = mCurrentTime;
                            mVibrator.vibrate(500);
                            prox = true;
                        }

                        //Iterating the ennemy vector
                        Iterator<Ennemy> it = mEnnemy.iterator();
                        while (it.hasNext()) {
                            Ennemy e = it.next();

                            //Getting Ennemy Path
                            Path ennemyPath = e.drawEnnemy();
                            //Getting Ennemy Hitbox
                            Region ennemyHitBox = new Region();
                            ennemyHitBox.setPath(ennemyPath, new Region(0, 0, this.getWidth(), this.getHeight()));

                            if (!hitThisLoop && !e.getHit()) {
                                //Checking for collision only if not hit this loop
                                if (!rocketHitBox.quickReject(ennemyHitBox) && rocketHitBox.op(ennemyHitBox, Region.Op.INTERSECT)) {
                                    //If not alread hit and currently hit, set "hit" to 1
                                    if (!mRocket.getHit()) {
                                        hitThisLoop = true;
                                        mRocket.setHit(true);
                                    }
                                }
                            }

                            if(mCurrentTime-mPrevEventTime>=3000) {
                                if (!mTouchHitBox.quickReject(ennemyHitBox) && mTouchHitBox.op(ennemyHitBox, Region.Op.INTERSECT)) {
                                    //Hit Ennemy if touched
                                    mVibrator.vibrate(50);
                                    e.setHit(true);
                                    mPrevEventTime = mCurrentTime;
                                }
                            }

                            if (prox) {
                                e.setHit(true);
                            }


                            //Drawing the Ennemy
                            canvas.drawPath(ennemyPath, e.getPaint());

                            //Updating the ennemy position
                            e.setPosition(new Point(e.getPosition().x, e.getPosition().y + e.getSpeed()));
                            if (e.getPosition().y > (this.getHeight() + 10)) {
                                //removing ennemy if it is out of bounds
                                it.remove();
                            }
                        }

                        if (!hitThisLoop && mRocket.getHit()) // If not hit by any ennemy and already hit, set "hit" to 0
                        {
                            mRocket.setHit(false);
                        }

                        //Drawing text
                        Paint textPaint = new Paint();
                        textPaint.setColor(Color.WHITE);
                        textPaint.setTextSize(80);
                        canvas.drawText("SCORE : " + String.valueOf(mScore), this.getWidth() / 2, 100, textPaint);

                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            else if (mPaused && !mEnded)
            {
                mTimeInPause = System.currentTimeMillis() - mBeginningTime - mCurrentTime;
            }
            else if (mEnded)
            {
                if (mSurfaceHolder.getSurface().isValid()) {
                    canvas = mSurfaceHolder.lockCanvas();

                    canvas.drawColor(mbgColor);

                    //Iterating the ennemy vector
                    Iterator<Ennemy> it = mEnnemy.iterator();
                    while (it.hasNext()) {
                        Ennemy e = it.next();

                        //Getting Ennemy Path
                        Path ennemyPath = e.drawEnnemy();

                        //Drawing the Ennemy
                        canvas.drawPath(ennemyPath, e.getPaint());

                        //Updating the ennemy position
                        e.setPosition(new Point(e.getPosition().x, e.getPosition().y + e.getSpeed()));
                        if (e.getPosition().y > (this.getHeight() + 10)) {
                            //removing ennemy if it is out of bounds
                            it.remove();
                        }
                    }

                    //Drawing text
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(80);
                    canvas.drawText("SCORE FINAL :",this.getWidth() / 2 - 250, this.getHeight()/2 - 300,textPaint);
                    canvas.drawText(String.valueOf(mScore), this.getWidth() / 2 - 50, this.getHeight()/2 - 200, textPaint);

                    Path sendScoreBox = new Path();
                    sendScoreBox.addRoundRect(100,this.getHeight()/2 - 100,this.getWidth()-100,this.getHeight()/2 + 100,20,20,Path.Direction.CW);
                    Region sendScoreHitBox = new Region();
                    sendScoreHitBox.setPath(sendScoreBox, new Region(0, 0, this.getWidth(), this.getHeight()));
                    if (!mTouchHitBox.quickReject(sendScoreHitBox) && mTouchHitBox.op(sendScoreHitBox, Region.Op.INTERSECT)) {
                        //Hit Ennemy if touched
                        sendSMS();
                    }
                    Paint boxPaint = new Paint();
                    boxPaint.setColor(Color.WHITE);
                    boxPaint.setShader(new LinearGradient(this.getWidth()/2,this.getHeight()/2 + 50,this.getWidth()/2,this.getHeight()/2 + 100,Color.WHITE,Color.LTGRAY, Shader.TileMode.CLAMP));
                    canvas.drawPath(sendScoreBox,boxPaint);

                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(60);
                    canvas.drawText("Tap here to send your score !", 150, this.getHeight()/2+20, textPaint);

                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    // Implementing onTouch reaction
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {

            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                float x = e.getX();
                float y = e.getY();

                Path touchPath = new Path();
                touchPath.addCircle(x, y, 50, Path.Direction.CW);

                // Creating hitbox to collide with ennemies
                mTouchHitBox.setPath(touchPath, new Region(0, 0, this.getWidth(), this.getHeight()));
                return true;
        }
        return false;
    }

    public void sendSMS()
    {
        mListener.smsCallback();
    }

    public boolean getPaused()
    {
        return mPaused;
    }

    public void setPaused(boolean p) { mPaused=p;}

    public int getScore()
    {
        return mScore;
    }

    public void onPauseClick()
    {
        mPaused=!mPaused;
    }

    public void onRestartClick()
    {
        init();
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
