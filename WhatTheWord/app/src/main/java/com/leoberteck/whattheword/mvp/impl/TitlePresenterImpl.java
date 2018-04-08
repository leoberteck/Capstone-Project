package com.leoberteck.whattheword.mvp.impl;

import android.databinding.Bindable;

import com.leoberteck.whattheword.BR;
import com.leoberteck.whattheword.entities.Score;
import com.leoberteck.whattheword.mvp.BasePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Title;

public class TitlePresenterImpl extends BasePresenterImpl implements Title.TitlePresenter {

    private String bestScore = "teste";

    @Bindable
    @Override
    public String getBestScore() {
        return bestScore;
    }

    @Bindable
    @Override
    public void setBestScore(Score bestScore) {
        this.bestScore = bestScore != null ? String.valueOf(bestScore.getScore()) : null;
        notifyPropertyChanged(BR.bestScore);
    }

    @Override
    public void onPlayClick() {

    }

    @Override
    public void onClearScoreClick() {

    }
}
