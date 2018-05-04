package com.leoberteck.whattheword.mvp.impl;


import android.content.ContentResolver;
import android.util.Log;
import android.view.animation.Animation;

import com.leoberteck.backend.wtwapi.model.WordEntry;
import com.leoberteck.whattheword.BR;
import com.leoberteck.whattheword.data.dao.AbstractDaoInterface;
import com.leoberteck.whattheword.data.dao.GameStatusDao;
import com.leoberteck.whattheword.data.dao.ScoreDao;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.mvp.BasePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Game;
import com.leoberteck.whattheword.utils.AbstractAnimationListener;
import com.leoberteck.wtw.WhatTheWord;
import com.leoberteck.wtw.entities.GameStatus;
import com.leoberteck.wtw.exceptions.GameOverException;

import java.util.List;

public class GamePresenterImpl extends BasePresenterImpl implements Game.GamePresenter {

    private static final String TAG = Game.GamePresenter.class.getSimpleName();
    private static final WhatTheWord whatTheWord = new WhatTheWord();

    private GameStatus gameStatus;
    private String guess;
    private Game.GameActivity gameActivity;

    @Override
    public Game.GameActivity getGameActivity() {
        return gameActivity;
    }

    @Override
    public void setGameActivity(Game.GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Override
    public String getLevel() {
        return String.valueOf(gameStatus != null ? gameStatus.getLevel() : 0);
    }

    @Override
    public String getScore() {
        return String.valueOf(gameStatus != null ? gameStatus.getScore() : 0);
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        notifyPropertyChanged(BR._all);
    }

    @Override
    public Boolean[] getLifes() {
        return gameStatus != null ?
            new Boolean[]{
            gameStatus.getHearts() > 0
            , gameStatus.getHearts() > 1
            , gameStatus.getHearts() > 2
            , gameStatus.getHearts() > 3
            , gameStatus.getHearts() > 4
        } : null;
    }

    @Override
    public String getDefinition() {
        return gameStatus != null ? gameStatus.getDefinition() : null;
    }

    @Override
    public String getHiddenWord() {
        StringBuilder hidden = new StringBuilder("");
        if(gameStatus != null){
            String word = gameStatus.getWord();
            List<Integer> revealed = gameStatus.getRevealedLetters();
            for(int x=0,count=word.length();x < count; x++){
                hidden.append(revealed.contains(x) ? word.charAt(x) : '_').append(" ");
            }
        }
        return hidden.toString();
    }

    @Override
    public String getGuess() {
        return guess;
    }

    @Override
    public void setGuess(String guess) {
        this.guess = guess;
        notifyPropertyChanged(BR.guess);
    }

    @Override
    public void resetGame(){
        this.gameStatus = null;
    }

    @Override
    public void initGame(WordEntry wordEntry){
        if(wordEntry != null){
            GameStatus gameStatus = whatTheWord.startNewGame(wordEntry.getWord(), wordEntry.getDefinition(), this.gameStatus);
            gameStatus.setId(wordEntry.getId());
            setGameStatus(gameStatus);
        }
    }

    @Override
    public void onGuessClick() {
        Log.d(TAG, "Comparing " + gameStatus.getWord() + " with " + guess);
        if(gameStatus.getWord().equalsIgnoreCase(guess)){
            final long lastScore = gameStatus.getScore();
            whatTheWord.calculateRightGuess(gameStatus);
            final long finalScore = gameStatus.getScore();

            saveOrUpdateScore(getGameActivity().getContentResolver(), finalScore, (ex, newHighScore) -> {
                getGameActivity().sendUpdateScoreBroadCast(finalScore);
                GamePresenterImpl.this.getGameActivity().showSuccessDialog(newHighScore, lastScore, finalScore, gameStatus.getWord(), (dialog, which) -> {
                    if (getGameActivity() != null) {
                        getGameActivity().publishScore(gameStatus.getScore());
                        getGameActivity().fireLoader();
                        setGuess(null);
                    }
                });
            });
        } else {
            try {
                whatTheWord.calculateWrongGuess(gameStatus);
            } catch (GameOverException e) {
                saveOrUpdateScore(getGameActivity().getContentResolver(), gameStatus.getScore()
                    , (ex, result) -> {
                        if(getGameActivity() != null){
                            getGameActivity().publishScore(gameStatus.getScore());
                            getGameActivity().sendUpdateScoreBroadCast(gameStatus.getScore());
                            getGameActivity().showGameOverDialog(gameStatus.getScore(), gameStatus.getWord(), (dialog, which) -> {
                                resetGame();
                                getGameActivity().fireLoader();
                                setGuess(null);
                            });
                        }
                    });
            }
            getGameActivity().animateImageView(gameStatus.getHearts(), new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    notifyPropertyChanged(BR.lifes);
                    notifyPropertyChanged(BR.hiddenWord);
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    @Override
    public void saveState(ContentResolver contentResolver, final AbstractDaoInterface.OnTaskFinishListener<GameStatusEntity> onTaskFinishListener) {
        if(gameStatus != null){
            final GameStatusEntity entity = new GameStatusEntity(gameStatus);
            //Clear previous states
            final GameStatusDao gameStatusDao = new GameStatusDao(contentResolver);
            gameStatusDao.clear((ex, result) -> {
                //Save new state
                gameStatusDao.insert(entity, onTaskFinishListener);
            });
        }
    }

    private void saveOrUpdateScore(ContentResolver contentResolver, long newScore, final AbstractDaoInterface.OnTaskFinishListener<Boolean> onTaskFinishListener){
        final ScoreDao scoreDao = new ScoreDao(contentResolver);
        scoreDao.findOne(null, null, (ex, result) -> {
            if(ex == null && result != null){
                if(result.getScore() < newScore){
                    result.setScore(newScore);
                    scoreDao.update(result, (ex1, result1) -> onTaskFinishListener.onTaskFinish(ex1, true));
                } else {
                    onTaskFinishListener.onTaskFinish(null, false);
                }
            } else {
                scoreDao.insert(new ScoreEntity(newScore), (ex12, result12) -> onTaskFinishListener.onTaskFinish(ex12, false));
            }
        });
    }
}