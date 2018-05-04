package com.leoberteck.whattheword.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.leoberteck.whattheword.R;
import com.leoberteck.whattheword.data.dao.AbstractDaoInterface;
import com.leoberteck.whattheword.data.dao.ScoreDao;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.view.TitleActivity;

public class PlayWidget extends AppWidgetProvider {
    public static String PLAY_WIDGET_BEST_SCORE_EXTRA = "com.leoberteck.whattheword.widget.set_best_score";

    private long bestScore = 0L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(PLAY_WIDGET_BEST_SCORE_EXTRA)){
            bestScore = intent.getLongExtra(PLAY_WIDGET_BEST_SCORE_EXTRA, 0L);
        }
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, long bestScore) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.play_widget);

        //Container click. Opens the app.
        Intent openAppIntent = new Intent(context, TitleActivity.class);
        PendingIntent openAppPI = PendingIntent.getActivity(
            context
            , 0
            , openAppIntent
            , PendingIntent.FLAG_UPDATE_CURRENT
        );
        views.setOnClickPendingIntent(
            R.id.container
            , openAppPI
        );
        views.setOnClickPendingIntent(
            R.id.fabPlay
            , openAppPI
        );

        //Sets the best score label value
        views.setTextViewText(R.id.textViewScore, String.valueOf(bestScore));

        //Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        AbstractDaoInterface.OnTaskFinishListener<ScoreEntity> callback = (ex, result) -> {
            //If a scoreEntity is found, replace bestScore value
            if(result != null){
                bestScore = result.getScore();
            }
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, bestScore);
            }
        };

        //If score has not been set by a broadcast, then try to find it.
        if(bestScore == 0L){
            ScoreDao scoreDao = new ScoreDao(context.getContentResolver());
            scoreDao.findOne(null, null, callback);
        } else {
            callback.onTaskFinish(null, null);
        }
    }
}

