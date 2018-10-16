package com.enib.cai.sensorrocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CustomSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new CustomSurfaceView(this);
        mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(mSurfaceView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSurfaceView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSurfaceView.onResume();
    }
}
