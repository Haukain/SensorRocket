package com.enib.cai.sensorrocket;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mSceneLayout;

    private LinearLayout mGameLayout;

    private LinearLayout mButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSceneLayout = new FrameLayout(this);

        mGameLayout = new LinearLayout(this);

        CustomSurfaceView gameView = new CustomSurfaceView(this);
        gameView.setId(1);
        mGameLayout.addView(gameView);

        mButtonLayout = new LinearLayout(this);

        final Context that = this;
        Button pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CustomSurfaceView)mGameLayout.findViewById(1)).onPauseClick();
                    }
                }
        );
        Button restartButton = new Button(this);
        restartButton.setText("Restart");
        restartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CustomSurfaceView)mGameLayout.findViewById(1)).onRestartClick();
                    }
                }
        );
        mButtonLayout.addView(pauseButton);
        mButtonLayout.addView(restartButton);

        mSceneLayout.addView(mGameLayout);
        mSceneLayout.addView(mButtonLayout);

        mSceneLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(mSceneLayout);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ((CustomSurfaceView)mGameLayout.findViewById(1)).onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ((CustomSurfaceView)mGameLayout.findViewById(1)).onResume();
    }
}
