package com.leoberteck.whattheword.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.leoberteck.whattheword.widget.PlayWidget;

public final class WidgetUtils {

    public static void updatePlayWidgetBestScore(Context context, long newScore){
        Intent intent = new Intent(context, PlayWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context.getApplicationContext())
                .getAppWidgetIds(new ComponentName(context.getApplicationContext(), PlayWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(PlayWidget.PLAY_WIDGET_BEST_SCORE_EXTRA, newScore);
        context.sendBroadcast(intent);
    }
}
