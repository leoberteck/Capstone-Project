package com.leoberteck.whattheword.utils;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.view.View;

import junit.framework.Assert;

import org.hamcrest.Matcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DelayedInteraction {

    public static ViewInteraction onView(Matcher<View> viewMatcher, int delay) {
        waitFor(delay);
        return Espresso.onView(viewMatcher);
    }

    private static void waitFor(int ms) {
        final CountDownLatch signal = new CountDownLatch(1);
        try {
            signal.await(ms, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
