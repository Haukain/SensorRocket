package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

public class Rocket {

    private float mRocketOffset;
    private Paint mPaint;
    private Point mPosition;
    private boolean mHit;

    private int mLife;

    public Rocket()
    {
        mRocketOffset = 0;
        mPosition = new Point(0,0);
        mHit = false;
        mLife = 1;

        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(255,200, 0, 24));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    public Path drawRocket() {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        /*
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(mPosition.x, mPosition.y);
        path.lineTo(mPosition.x - 27, mPosition.y);
        path.lineTo(mPosition.x - 26, mPosition.y - 50);
        path.lineTo(mPosition.x - 25, mPosition.y - 100);
        path.lineTo(mPosition.x -10, mPosition.y - 145);
        path.lineTo(mPosition.x, mPosition.y - 150);
        path.lineTo(mPosition.x + 10, mPosition.y - 145);
        path.lineTo(mPosition.x + 25, mPosition.y - 100);
        path.lineTo(mPosition.x + 26, mPosition.y - 50);
        path.lineTo(mPosition.x + 27, mPosition.y);
        path.lineTo(mPosition.x, mPosition.y);
        path.close();
        */

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

    public void setPaint(int alpha,int red,int green,int blue) // Setting paint to argb(a,r,g,b)
    {
        mPaint.setColor(Color.argb(alpha,red,green,blue));
    }

    public void setPaint() // Setting paint to default paint (255,255,0,0)
    {
        mPaint.setColor(Color.argb(255,200, 0, 24));
    }

    public boolean getHit() // Getting hit boolean
    {
        return mHit;
    }

    public void setHit(boolean state) // Setting hit boolean and changing paint to "hit" paint
    {
        mHit = state;
        if(state==true)
        {
            mLife--;
        }
    }

    public boolean isAlive()
    {
        return (mLife>0);
    }

}
