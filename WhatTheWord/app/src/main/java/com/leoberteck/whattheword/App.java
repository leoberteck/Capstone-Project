package com.leoberteck.whattheword;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

public class App extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
