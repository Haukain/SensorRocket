package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

public class Ennemy {

    private int mSpeed;
    private int mSize;
    private int mOffset;
    private Point mPosition;
    private Paint mPaint;
    private boolean mHit;

    public Ennemy(int maxWidth)
    {
        mSpeed = (int)(Math.random()*8 + 5);
        mSize = (int)(Math.random()*50 + 30);
        mOffset = (int)(Math.random()*mSize/2);
        mPosition = new Point((int)(Math.random()*maxWidth),-80);

        mHit = false;

        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(255,245+(int)(Math.random()*20-10), 215+(int)(Math.random()*20-10), 0+(int)(Math.random()*20-10)));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    public Path drawEnnemy()
    {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(mPosition.x-mSize/2, mPosition.y-mOffset);
        path.lineTo(mPosition.x+mOffset,mPosition.y-mSize/2);
        path.lineTo(mPosition.x+mSize/2,mPosition.y+mOffset);
        path.lineTo(mPosition.x-mOffset,mPosition.y+mSize/2);
        path.lineTo(mPosition.x-mSize/2, mPosition.y-mOffset);

        path.close();

        return path;
    }

    public int getSpeed()
    {
        return mSpeed;
    }

    public int getSize()
    {
        return mSize;
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

    public void setPaint(int a,int r,int g, int b)
    {
        mPaint.setColor(Color.argb(a,r,g,b));
    }

    public void setHit(boolean state) {
        mHit = state;
        mPaint.setAlpha(50);
    }

    public boolean getHit() { return mHit; }
}
