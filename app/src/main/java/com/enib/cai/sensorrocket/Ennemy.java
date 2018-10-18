package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class Ennemy {

    private int mSpeed;
    private int mSize;
    private int mOffset;
    private Point mPosition;
    private Paint mPaint;

    public Ennemy(int maxWidth)
    {
        mSpeed = (int)(Math.random()*8 + 5);
        mSize = (int)(Math.random()*50 + 30);
        mOffset = (int)(Math.random()*mSize/2);
        mPosition = new Point((int)(Math.random()*maxWidth),-80);

        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(255,76+(int)(Math.random()*20-10), 64+(int)(Math.random()*20-10), 51+(int)(Math.random()*20-10)));
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
}
