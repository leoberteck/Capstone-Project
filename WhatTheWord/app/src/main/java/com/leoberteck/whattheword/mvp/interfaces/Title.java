package com.leoberteck.whattheword.mvp.interfaces;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.databinding.Bindable;

import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.mvp.BasePresenter;
import com.leoberteck.wtw.entities.GameStatus;

public interface Title {
    interface TitleActivity {
        void navigateToGameActivity(GameStatus gameStatus);
        ContentResolver getContentResolver();
        void showConfirmationDialog(DialogInterface.OnClickListener onConfirmListener);
        void loadScore();

        void sendUpdateScoreBroadCast(long newScore);

        void showLeaderBoards();
    }

    interface TitlePresenter extends BasePresenter {
        TitleActivity getActivity();
        void setActivity(TitleActivity titleActivity);

        @Bindable
        String getBestScore();
        @Bindable
        void setBestScore(ScoreEntity data);

        void onPlayClick();
        void onClearScoreClick();
        void onShowLeaderboardsClick();
    }
}