package com.example.defensecommanderapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;


class ScrollingBackground {

    private final Context context;
    private final ViewGroup layout;
    private ImageView backImageA;
    private ImageView backImageB;
    private final long duration;
    private final int resId;

    private int screenHeight;
    private int screenWidth;

    ScrollingBackground(Context context, ViewGroup layout, int resId, long duration, int screenWidth, int screenHeight) {
        this.context = context;
        this.layout = layout;
        this.resId = resId;
        this.duration = duration;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setupBackground();
    }

    private void setupBackground() {
        backImageA = new ImageView(context);
        backImageB = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(screenWidth + getBarHeight(), screenHeight);
        backImageA.setLayoutParams(params);
        backImageB.setLayoutParams(params);

        layout.addView(backImageA);
        layout.addView(backImageB);

        Bitmap backBitmapA = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap backBitmapB = BitmapFactory.decodeResource(context.getResources(), resId);

        backImageA.setImageBitmap(backBitmapA);
        backImageB.setImageBitmap(backBitmapB);

        backImageA.setScaleType(ImageView.ScaleType.FIT_XY);
        backImageB.setScaleType(ImageView.ScaleType.FIT_XY);


        ObjectAnimator fadeInAndOutCloud1 = ObjectAnimator.ofFloat(backImageA, "alpha",(float) 0.25,(float) 0.95);
        fadeInAndOutCloud1.setDuration(20000);
        fadeInAndOutCloud1.setRepeatMode(ValueAnimator.REVERSE);
        fadeInAndOutCloud1.setRepeatCount(ValueAnimator.INFINITE);
        fadeInAndOutCloud1.start();

        ObjectAnimator fadeInAndOutCloud2 = ObjectAnimator.ofFloat(backImageB, "alpha",(float) 0.25,(float) 0.95);
        fadeInAndOutCloud2.setDuration(20000);
        fadeInAndOutCloud2.setRepeatMode(ValueAnimator.REVERSE);
        fadeInAndOutCloud2.setRepeatCount(ValueAnimator.INFINITE);
        fadeInAndOutCloud2.start();

        backImageA.setZ(1);
        backImageB.setZ(1);
        //backImageA.setAlpha((float) 0.3);
        //backImageB.setAlpha((float) 0.3);

        animateBack();
    }

    private void animateBack() {

        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                float width = screenWidth + getBarHeight();

                float a_translationX = width * progress;
                float b_translationX = width * progress - width;

                backImageA.setTranslationX(a_translationX);
                backImageB.setTranslationX(b_translationX);
            }
        });
        animator.start();
    }


    private int getBarHeight() {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
