package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Ennemy {

    private int mSpeed;
    private int mSize;
    private Point mPosition;
    private Paint mPaint;

    public Ennemy(int maxWidth,int offset)
    {
        mSpeed = 5;
        mSize = 40;
        mPosition = new Point(offset*(maxWidth/100),-20);

        mPaint = new Paint();
        mPaint.setColor(Color.argb(255,50, 255, 50));
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
}
