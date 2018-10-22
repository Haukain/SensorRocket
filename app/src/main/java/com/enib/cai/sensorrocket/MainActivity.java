package com.enib.cai.sensorrocket;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements GameListener {

    private FrameLayout mSceneLayout;

    private LinearLayout mGameLayout;

    private LinearLayout mButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSceneLayout = new FrameLayout(this);

        mGameLayout = new LinearLayout(this);

        CustomSurfaceView gameView = new CustomSurfaceView(this,this);
        gameView.setId(1);
        mGameLayout.addView(gameView);

        mButtonLayout = new LinearLayout(this);

        final Button pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setId(1);
        pauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CustomSurfaceView)mGameLayout.findViewById(1)).onPauseClick();
                        if (((CustomSurfaceView)mGameLayout.findViewById(1)).getPaused()) pauseButton.setText("Play");
                        else pauseButton.setText("Pause");
                    }
                }
        );
        Button restartButton = new Button(this);
        restartButton.setText("Restart");
        pauseButton.setId(2);
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
        ((CustomSurfaceView)mGameLayout.findViewById(1)).setPaused(true);
        ((CustomSurfaceView)mGameLayout.findViewById(1)).onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ((CustomSurfaceView)mGameLayout.findViewById(1)).onResume();
    }

    @Override
    public void smsCallback() {

        String msg = "I just played SensorRocket and got " + String.valueOf(((CustomSurfaceView)mGameLayout.findViewById(1)).getScore()) + " points, check out this app !";

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));

        sendIntent.putExtra("sms_body", msg);

        startActivity(sendIntent);
    }

    @Override
    public void rocketHitCallback()
    {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.death_sound);
        mp.start();
    }
}
