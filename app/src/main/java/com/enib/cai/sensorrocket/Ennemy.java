package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class Ennemy {

    private int mSpeed;
    private int mSize;
    private Point mPosition;
    private Paint mPaint;

    public Ennemy(int maxWidth)
    {
        mSpeed = (int)(Math.random()*8 + 5);
        mSize = (int)(Math.random()*50 + 30);
        mPosition = new Point((int)(Math.random()*maxWidth),-80);

        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(255,0, 255, 0));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    public Path drawEnnemy()
    {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(mPosition.x, mPosition.y);
        path.addRect(mPosition.x,mPosition.y,mPosition.x+mSize,mPosition.y+mSize,Path.Direction.CW);
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
