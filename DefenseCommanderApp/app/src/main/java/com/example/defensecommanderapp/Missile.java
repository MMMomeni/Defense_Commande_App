package com.example.defensecommanderapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

class Missile {

    private final MainActivity mainActivity;
    private final ImageView imageView;
    private final AnimatorSet aSet = new AnimatorSet();
    private final int screenHeight;
    private final int screenWidth;
    private final long screenTime;
    private static final String TAG = "Plane";

    private List<Float> basesLocations = new ArrayList<>();

    private boolean hit = false;

    private final boolean launcher1Died;
    private final boolean launcher2Died;
    private final boolean launcher3Died;
    private final MissileMaker missileMaker;


    Missile(int screenWidth, int screenHeight, long screenTime,final MainActivity mainActivity, List<Float> basesLocations,
            boolean launcher1Died, boolean launcher2Died, boolean launcher3Died, MissileMaker missileMaker) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.screenTime = screenTime;
        this.mainActivity = mainActivity;
        this.basesLocations = basesLocations;

        this.launcher1Died = launcher1Died;
        this.launcher2Died = launcher2Died;
        this.launcher3Died = launcher3Died;

        this.missileMaker = missileMaker;


        imageView = new ImageView(mainActivity);
        imageView.setX(-500);

        mainActivity.runOnUiThread(() -> mainActivity.getLayout().addView(imageView));
    }

    AnimatorSet setData(int drawId){
        imageView.setImageResource(drawId);
        int startX = (int) (Math.random() * screenWidth);
        double endX = Math.random() * screenWidth;
        float angle = calculateAngle(startX, -200, endX, screenHeight + 500);
        imageView.setRotation(180 - angle);


        ObjectAnimator xAnim = ObjectAnimator.ofFloat(imageView, "x", startX,(float) endX);
        xAnim.setInterpolator(new LinearInterpolator());
        xAnim.setDuration((long) (screenTime ));
        xAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.runOnUiThread(() ->{
                        mainActivity.getLayout().removeView(imageView);
                        if (!hit) {
                            interceptorBlast((float) endX, screenHeight - 50);


                            boolean hitBase = false;

                            View launcher1 = mainActivity.getLayout().getViewById(R.id.launcher1);
                            View launcher2 = mainActivity.getLayout().getViewById(R.id.launcher2);
                            View launcher3 = mainActivity.getLayout().getViewById(R.id.launcher3);

                            if (!launcher1Died) {
                                if (endX >= launcher1.getX() - 100 && endX <= launcher1.getX() + 100) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    missileMaker.basesState("launcher1");
                                    baseBlast((float) endX, screenHeight -150);
                                    hitBase = true;
                                    stop();
                                    mainActivity.runOnUiThread(() ->
                                            mainActivity.launcherStamina("launcher1"));

                                }
                            }
                            if (!launcher2Died) {
                                if (endX >= launcher2.getX() - 100 && endX <= launcher2.getX() + 100) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    baseBlast((float) endX, screenHeight -150);
                                    missileMaker.basesState("launcher2");
                                    hitBase = true;
                                    stop();
                                    mainActivity.runOnUiThread(() ->
                                            mainActivity.launcherStamina("launcher2"));

                                }
                            }
                            if (!launcher3Died) {
                                if (endX >= launcher3.getX() - 100 && endX <= launcher3.getX() + 100) {
                                    SoundPlayer.getInstance().start("base_blast");
                                    baseBlast((float) endX, screenHeight -150);
                                    missileMaker.basesState("launcher3");
                                    hitBase = true;
                                    stop();
                                    mainActivity.runOnUiThread(() ->
                                            mainActivity.launcherStamina("launcher3"));

                                }
                            }
                            if (!hitBase) {
                                SoundPlayer.getInstance().start("missile_miss");

                            }


                        }

                });
                super.onAnimationEnd(animation);
            }
        });

        ObjectAnimator yAnim = ObjectAnimator.ofFloat(imageView, "y", -200, (screenHeight -100));
        yAnim.setInterpolator(new LinearInterpolator());
        yAnim.setDuration((long) (screenTime));

        aSet.playTogether(xAnim, yAnim);
        return aSet;
    }


    public static float calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;
        return (float) (angle);
    }

    void stop() {
        aSet.cancel();
    }

    float getX() {
        return imageView.getX();
    }

    float getY() {
        return imageView.getY();
    }

    float getSize() {
        return imageView.getWidth();
    }

    float getWidth() {
        return imageView.getWidth();
    }

    float getHeight() {
        return imageView.getHeight();
    }

    void baseBlast(float x, float y){
        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.blast);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset);
        iv.setRotation((float) (360.0 * Math.random()));

        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);

        alpha.start();
    }


    void interceptorBlast(float x, float y) {
        hit = true;


        final ImageView iv = new ImageView(mainActivity);
        iv.setImageResource(R.drawable.explode);

        iv.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        iv.setX(x - offset);
        iv.setY(y - offset);
        iv.setRotation((float) (360.0 * Math.random()));

        aSet.cancel();

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(iv);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }
        });
        alpha.start();
    }
}
