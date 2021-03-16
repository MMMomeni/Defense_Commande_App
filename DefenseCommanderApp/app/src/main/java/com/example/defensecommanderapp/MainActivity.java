package com.example.defensecommanderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private int screenHeight;
    private int screenWidth;

    private MissileMaker missileMaker;
    private TextView levelText;
    private TextView scoreText;

    private ImageView launcher1;
    private ImageView launcher2;
    private ImageView launcher3;
    private boolean launcher1Alive = true;
    private boolean launcher2Alive = true;
    private boolean launcher3Alive = true;

    private double interceptorBlast;

    private final List<Float> basesLocations = new ArrayList<>();

    private ImageView gameOverLogo;

    private boolean alreadyStopped = false;

    private int level;
    private int score;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        levelText = findViewById(R.id.levelText);
        scoreText = findViewById(R.id.scoreText);
        launcher1 = findViewById(R.id.launcher1);
        launcher2 = findViewById(R.id.launcher2);
        launcher3 = findViewById(R.id.launcher3);
        gameOverLogo = findViewById(R.id.gameOverLogo);
        gameOverLogo.setVisibility(View.INVISIBLE);

        setUIVisibility();
        getScreenDimensions();

        new ScrollingBackground(this,
                layout, R.drawable.clouds, 20000, screenWidth, screenHeight);



        layout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });


        basesLocations.add(launcher1.getX());
        basesLocations.add(launcher2.getX());
        basesLocations.add(launcher3.getX());
        missileMaker = new MissileMaker(this, screenWidth, screenHeight, basesLocations);
        new Thread(missileMaker).start();

    }

    //public void getBaseLocations


    public void handleTouch(float xLoc, float yLoc) {

        float launcher1X = launcher1.getX();
        float launcher1Y = launcher1.getY();

        float launcher2X = launcher2.getX();
        float launcher2Y = launcher2.getY();

        float launcher3X = launcher3.getX();
        float launcher3Y = launcher3.getY();

        float distanceToLauncher1 = Math.abs(launcher1X - xLoc);
        float distanceToLauncher2 = Math.abs(launcher2X - xLoc);
        float distanceToLauncher3 = Math.abs(launcher3X - xLoc);

        if (launcher1Alive && launcher2Alive && launcher3Alive) {
            if (distanceToLauncher1 < distanceToLauncher2 && distanceToLauncher1 < distanceToLauncher3){
                Interceptor i = new Interceptor(this,  launcher1X + (float) (launcher1.getWidth() * 0.2) , launcher1Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }

            else if (distanceToLauncher2 < distanceToLauncher1 && distanceToLauncher2 < distanceToLauncher3){
                Interceptor i = new Interceptor(this,  launcher2X + (float) (launcher2.getWidth() * 0.2), launcher2Y, xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
            else{
                Interceptor i = new Interceptor(this,  launcher3X + (float) (launcher3.getWidth() * 0.2), launcher3Y, xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }

        }

        if (launcher1Alive && launcher2Alive && !launcher3Alive){
            if (distanceToLauncher1 < distanceToLauncher2 ){
                Interceptor i = new Interceptor(this,  launcher1X + (float) (launcher1.getWidth() * 0.2) , launcher1Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
            else {
                Interceptor i = new Interceptor(this,  launcher2X + (float) (launcher2.getWidth() * 0.2) , launcher2Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
        }

        if (launcher1Alive && !launcher2Alive && launcher3Alive){
            if (distanceToLauncher1 < distanceToLauncher3 ){
                Interceptor i = new Interceptor(this,  launcher1X + (float) (launcher1.getWidth() * 0.2) , launcher1Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
            else {
                Interceptor i = new Interceptor(this,  launcher3X + (float) (launcher3.getWidth() * 0.2) , launcher3Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
        }

        if (!launcher1Alive && launcher2Alive && launcher3Alive){
            if (distanceToLauncher2 < distanceToLauncher3 ){
                Interceptor i = new Interceptor(this,  launcher2X + (float) (launcher2.getWidth() * 0.2) , launcher2Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
            else {
                Interceptor i = new Interceptor(this,  launcher3X + (float) (launcher3.getWidth() * 0.2) , launcher3Y , xLoc, yLoc);
                SoundPlayer.getInstance().start("launch_interceptor");
                i.launch();
            }
        }

        if (launcher1Alive && !launcher2Alive && !launcher3Alive){
            Interceptor i = new Interceptor(this,  launcher1X + (float) (launcher1.getWidth() * 0.2) , launcher1Y , xLoc, yLoc);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();
        }

        if (!launcher1Alive && launcher2Alive && !launcher3Alive){
            Interceptor i = new Interceptor(this,  launcher2X + (float) (launcher2.getWidth() * 0.2) , launcher2Y , xLoc, yLoc);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();
        }

        if (!launcher1Alive && !launcher2Alive && launcher3Alive){
            Interceptor i = new Interceptor(this,  launcher3X + (float) (launcher3.getWidth() * 0.2) , launcher3Y , xLoc, yLoc);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();
        }


    }

    public static float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (angle);
    }


    public void applyInterceptorBlast(Interceptor interceptor) {
        missileMaker.applyInterceptorBlast(interceptor);
        interceptorBlast++;
        //double acc = (double) scoreValue / interceptorBlast;
        //accuracy.setText(String.format(Locale.getDefault(),
         //       "Accuracy: %.0f%%", acc * 100.0));
    }

    public void launcherStamina(String s){
        if (s.equals("launcher1")){
            launcher1Alive = false;
            launcher1.setVisibility(View.INVISIBLE);
        }

        if (s.equals("launcher2")){
            launcher2Alive = false;
            launcher2.setVisibility(View.INVISIBLE);
        }

        if (s.equals("launcher3")){
            launcher3Alive = false;
            launcher3.setVisibility(View.INVISIBLE);
        }

        if (!launcher1Alive && !launcher2Alive && !launcher3Alive && !alreadyStopped){
            alreadyStopped = true;
            missileMaker.setRunning(false);
            gameOverLogo.setVisibility(View.VISIBLE);
            gameOverLogo.setZ(2);
            ObjectAnimator gameOver = ObjectAnimator.ofFloat(gameOverLogo, "alpha", 0, 1);
            gameOver.setDuration(4500);
            gameOver.start();


            new Handler().postDelayed(() -> {
                Intent i = new Intent(this, ScoreBoardActivity.class);
                i.putExtra("score", score);
                i.putExtra("level", level);
                startActivityForResult(i,1);
                //overridePendingTransition(); // new act, old act
                finish();
            }, 7500);

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();

    }

    private void setUIVisibility(){
        View decorView = getWindow().getDecorView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
    }


    public ConstraintLayout getLayout() {
        return layout;
    }

    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    public void setLevel(final int i) {
        level = i;
        runOnUiThread(() -> levelText.setText(String.format(Locale.getDefault(), "Level: %d", i)));
    }

    public void setScore(final int i) {
        score = i;
        runOnUiThread(() -> scoreText.setText(String.format(Locale.getDefault(), "%d", i)));
    }


    public void doStop(View v) {
        missileMaker.setRunning(false);
        finish();
    }




}