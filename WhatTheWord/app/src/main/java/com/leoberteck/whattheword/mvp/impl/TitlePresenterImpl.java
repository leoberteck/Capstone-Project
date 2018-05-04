package com.leoberteck.whattheword.mvp.impl;

import android.databinding.Bindable;

import com.leoberteck.whattheword.data.dao.ScoreDao;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.mvp.BasePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Title;

import java.util.Objects;

public class TitlePresenterImpl extends BasePresenterImpl implements Title.TitlePresenter {

    private String bestScore = "teste";
    private Title.TitleActivity titleActivity;

    @Override
    public Title.TitleActivity getActivity() {
        return titleActivity;
    }

    @Override
    public void setActivity(Title.TitleActivity titleActivity) {
        this.titleActivity = titleActivity;
    }

    @Bindable
    @Override
    public String getBestScore() {
        return bestScore;
    }

    @Bindable
    @Override
    public void setBestScore(ScoreEntity bestScore) {
        this.bestScore = bestScore != null ? String.valueOf(bestScore.getScore()) : null;
        notifyChange();
    }

    @Override
    public void onPlayClick() {
        Objects.requireNonNull(getActivity()).navigateToGameActivity(null);
    }

    @Override
    public void onClearScoreClick() {
        getActivity().showConfirmationDialog((dialog, which) -> {
            ScoreDao scoreDao = new ScoreDao(TitlePresenterImpl.this.getActivity().getContentResolver());
            scoreDao.clear((ex, result) -> {
                TitlePresenterImpl.this.getActivity().sendUpdateScoreBroadCast(0L);
                TitlePresenterImpl.this.getActivity().loadScore();
            });
        });
    }

    @Override
    public void onShowLeaderboardsClick() {
        getActivity().showLeaderBoards();
    }
}
