package com.example.androiddevelopment.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by asus on 2016/6/17.
 * 动画工具类
 */
public class AnimUtils {
    public static void scale(final View v, float startScale, float endScale, long du, Animator.AnimatorListener finishListener) {
        v.clearAnimation();
        if (startScale != endScale) {
            ValueAnimator anim = ValueAnimator.ofFloat(startScale, endScale);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    v.setScaleX(value);
                    v.setScaleY(value);
                }

            });
            anim.setInterpolator(new OvershootInterpolator());
            anim.setDuration(du <= 0 ? 700 : du);
            if (finishListener != null) {
                anim.addListener(finishListener);
            }
            anim.start();
        } else {
            AnimatorSet set = new AnimatorSet();
            ValueAnimator anim1 = ValueAnimator.ofFloat(startScale, endScale - 0.3F);
            ValueAnimator anim2 = ValueAnimator.ofFloat(endScale - 0.3F, endScale);
            anim1.setInterpolator(new OvershootInterpolator());
            anim2.setInterpolator(new OvershootInterpolator());
            anim1.setDuration(300);
            anim2.setDuration(300);
            set.play(anim1).before(anim2);
            ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    v.setScaleX(value);
                    v.setScaleY(value);
                }

            };
            anim1.addUpdateListener(listener);
            anim2.addUpdateListener(listener);
            if (finishListener != null) {
                set.addListener(finishListener);
            }
            set.start();
        }
    }

    public static void scale(final View v, float startScale, float endScale) {
        scale(v, startScale, endScale, null);
    }

    public static void scale(final View v, float startScale, float endScale, Animator.AnimatorListener finishListener) {
        scale(v, startScale, endScale, -1, null);
    }

}
