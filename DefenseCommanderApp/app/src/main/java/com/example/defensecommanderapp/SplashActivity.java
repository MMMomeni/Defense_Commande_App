package com.example.defensecommanderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;
    private MediaPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast, false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast, false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile, false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor, false);
        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile, false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss, false);

        player = MediaPlayer.create(this, R.raw.background);
        player.setLooping(true); // Set looping
        //player.setVolume(100,100);
        player.start();

        ImageView appLogo = findViewById(R.id.appLogo);

        ObjectAnimator logoAnim = ObjectAnimator.ofFloat(appLogo, "alpha", 0, 1);
        logoAnim.setDuration(3000);
        logoAnim.start();


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        SoundPlayer.getInstance().start("background");
        new Handler().postDelayed(() -> {


            //SoundPlayer.getInstance().start("background");
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i =
                    new Intent(this, MainActivity.class);
            startActivity(i);
            //overridePendingTransition(); // new act, old act
            // close this activity
            finish();

        }, SPLASH_TIME_OUT);

    }
}