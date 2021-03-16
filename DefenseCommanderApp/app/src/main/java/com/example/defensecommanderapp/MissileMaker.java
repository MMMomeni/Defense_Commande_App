package com.example.defensecommanderapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MissileMaker implements Runnable {
    private final MainActivity mainActivity;
    private final int screenWidth;
    private final int screenHeight;
    private boolean isRunning = true;
    private final ArrayList<Missile> activeMissiles = new ArrayList<>();

    private int count = 0;
    private static final int LEVEL_COUNT = 3;
    private int level = 1;
    private int score = 0;
    private long delay = 4000;
    private static final int INTERCEPTOR_BLAST_RANGE = 150;
    private List<Float> basesLocations = new ArrayList<>();

    private boolean launcher1Died = false;
    private boolean launcher2Died = false;
    private boolean launcher3Died = false;

    private boolean alreadyStopped = false;




    MissileMaker(MainActivity mainActivity, int screenWidth, int screenHeight, List<Float> basesLocations){
        this.mainActivity = mainActivity;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.basesLocations = basesLocations;
        mainActivity.setLevel(1);
        mainActivity.setScore(0);
    }

    void setRunning(boolean running) {
        if (!alreadyStopped) {
            alreadyStopped = true;
            isRunning = running;
            for (Missile p : activeMissiles) {
                if (p != null)
                p.stop();
            }
        }
    }

    @Override
    public void run() {
        //setRunning(true);
        while (isRunning){

            count++;
            int resID = R.drawable.missile;

            final Missile missile = new Missile(screenWidth, screenHeight, delay, mainActivity, basesLocations,
                    launcher1Died, launcher2Died, launcher3Died, this);
            activeMissiles.add(missile);
            final AnimatorSet as = missile.setData(resID);
            as.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    activeMissiles.remove(missile);


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            mainActivity.runOnUiThread(() -> {
                as.start();
                SoundPlayer.getInstance().start("launch_missile");
            });



            if (count > LEVEL_COUNT){
                level++;
                mainActivity.setLevel(level);
                delay -= 250;

                if (delay < 250)
                    delay = 250;

                count = 0;
            }
            try {
                Thread.sleep((long) (delay * 0.7));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void removePlaneFromList(){

    }

    public void basesState(String s){
        if (s.equals("launcher1"))
            launcher1Died = true;
        if (s.equals("launcher2"))
            launcher2Died = true;
        if (s.equals("launcher3"))
            launcher3Died = true;
    }

    void applyInterceptorBlast(Interceptor interceptor) {

        float interceptorX = interceptor.getX();
        float interceptorY = interceptor.getY();

        ArrayList<Missile> nowGone = new ArrayList<>();
        ArrayList<Missile> temp = new ArrayList<>(activeMissiles);

        for (Missile m : temp) {
            float planeX = (int) (m.getX() + (0.5 * m.getWidth()));
            float planeY = (int) (m.getY() + (0.5 * m.getHeight()));
            float distanceToMissile = (float) Math.sqrt((planeY - interceptorY) * (planeY - interceptorY) + (planeX - interceptorX) * (planeX - interceptorX));
            //float distanceToBase1 = (float) Math.sqrt((planeY - interceptorY) * (planeY - interceptorY) + (planeX - interceptorX) * (planeX - interceptorX));

            if (distanceToMissile < INTERCEPTOR_BLAST_RANGE) {
                //SoundPlayer.start("interceptor_hit_plane");
                //mainActivity.incrementScore();
                m.interceptorBlast(planeX, planeY);
                SoundPlayer.getInstance().start("interceptor_hit_missile");
                score++;
                nowGone.add(m);
                mainActivity.runOnUiThread(() ->
                        mainActivity.setScore(score));
            }
        }

        for (Missile m : nowGone) {
            activeMissiles.remove(m);
        }
    }
}
