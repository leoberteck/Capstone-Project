package com.leoberteck.whattheword.mvp.interfaces;

import android.databinding.Bindable;

import com.leoberteck.whattheword.entities.Score;
import com.leoberteck.whattheword.mvp.BasePresenter;

public interface Title {
    interface TitleActivity {

    }

    interface TitlePresenter extends BasePresenter {
        @Bindable
        String getBestScore();
        @Bindable
        void setBestScore(Score data);

        void onPlayClick();
        void onClearScoreClick();
    }
}