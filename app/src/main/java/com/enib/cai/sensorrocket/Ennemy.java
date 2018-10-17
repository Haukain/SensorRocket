package com.enib.cai.sensorrocket;

import android.graphics.Point;

public class Ennemy {

    private int mSpeed;
    private int mSize;
    private Point mPosition;

    public Ennemy(int maxWidth)
    {
        mSpeed = 50;
        mSize = 30;
        mPosition = new Point(maxWidth/2,-20);
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
}
