package com.leoberteck.whattheword.utils;

import android.animation.ValueAnimator;
import android.widget.TextView;

public class AnimationUtils {
    public static void animateRange(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        animateRange(textview, valueAnimator, 1500);
    }

    public static void animateRange(final TextView textview, Object... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject((fraction, startValue, endValue) -> fraction < 0.5?startValue:endValue, values);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        animateRange(textview, valueAnimator, 1500);
    }

    private static void animateRange(final TextView textView, ValueAnimator animator, int duration){
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> textView.setText(String.valueOf(animation.getAnimatedValue())));
        animator.start();
    }
}
