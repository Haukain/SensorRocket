package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

public class Rocket {

    private Paint mPaint;
    private Paint mStrokePaint;
    private Point mPosition;
    private boolean mHit;

    public Rocket()
    {
        mPosition = new Point(0,0);
        mHit = false;

        mPaint = new Paint();
        mPaint.setColor(Color.argb(255,200, 0, 24));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mStrokePaint = new Paint();
        mStrokePaint.setStrokeWidth(4);
        mStrokePaint.setColor(Color.argb(200,255, 255, 255));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
    }

    public Path drawRocket() {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        RectF oval = new RectF(mPosition.x-25,mPosition.y-150,mPosition.x+25,mPosition.y+150);
        RectF rightOval =  new RectF(mPosition.x,mPosition.y,mPosition.x+50,mPosition.y+150);
        RectF leftOval =  new RectF(mPosition.x-50,mPosition.y,mPosition.x,mPosition.y+150);

        path.arcTo(oval,180F,90F,true);
        path.arcTo(oval,270F,90F);
        path.arcTo(rightOval,270F,90F);
        path.lineTo(mPosition.x,mPosition.y+25);
        path.lineTo(mPosition.x-50,mPosition.y+75);
        path.arcTo(leftOval,180F,90F);
        path.close();

        return path;
    }

    public Point getPosition()
    {
        return mPosition;
    }

    public void setPosition(Point newPos)
    {

        mPosition = newPos;
    }

    public Paint getPaint()
    {
        return mPaint;
    }

    public Paint getStrokePaint() { return mStrokePaint; }

    public void setHit() // Setting hit boolean and changing paint to "hit" paint
    {
        mHit = true;
    }

    public boolean isAlive()
    {
        return (!mHit);
    }

}
