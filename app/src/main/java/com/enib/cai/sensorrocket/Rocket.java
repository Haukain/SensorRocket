package com.enib.cai.sensorrocket;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Rocket {

    private float mRocketOffset;
    private Paint mPaint;

    public Rocket()
    {
        mRocketOffset = 0;

        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.argb(255,255, 0, 0));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    public Path drawRocket(float screenWidth, float screenHeight, float offset) {
        Path path = new Path();
        mRocketOffset += offset;
        if(mRocketOffset<(-screenWidth/2))
        {
            mRocketOffset = -screenWidth/2;
        }
        if(mRocketOffset>screenWidth/2)
        {
            mRocketOffset = screenWidth/2;
        }
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(screenWidth/2 + mRocketOffset, screenHeight-150);
        path.lineTo(screenWidth/2-27 + mRocketOffset, screenHeight-150);
        path.lineTo(screenWidth/2-26 + mRocketOffset, screenHeight-200);
        path.lineTo(screenWidth/2-25 + mRocketOffset, screenHeight-250);
        path.lineTo(screenWidth/2-10 + mRocketOffset, screenHeight-295);
        path.lineTo(screenWidth/2 + mRocketOffset, screenHeight-300);
        path.lineTo(screenWidth/2+10 + mRocketOffset, screenHeight-295);
        path.lineTo(screenWidth/2+25 + mRocketOffset, screenHeight-250);
        path.lineTo(screenWidth/2+26 + mRocketOffset, screenHeight-200);
        path.lineTo(screenWidth/2+27 + mRocketOffset, screenHeight-150);
        path.lineTo(screenWidth/2 + mRocketOffset, screenHeight-150);
        path.close();

        return path;
    }

    public Paint getPaint()
    {
        return mPaint;
    }
}
