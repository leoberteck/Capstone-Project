package com.leoberteck.whattheword.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public interface NetworkUtilsIterface {

    default boolean isOnline(Context context){
        boolean isOnline = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                isOnline = true;
            }
        }
        return isOnline;
    }
}
