package com.leoberteck.whattheword.mvp.interfaces;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.databinding.Bindable;
import android.view.animation.Animation;

import com.leoberteck.backend.wtwapi.model.WordEntry;
import com.leoberteck.whattheword.data.dao.AbstractDaoInterface;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;
import com.leoberteck.whattheword.mvp.BasePresenter;
import com.leoberteck.wtw.entities.GameStatus;

public interface Game {

    interface GameActivity {

        void fireLoader();

        void showSuccessDialog(boolean newHighScore, long lastScore, long finalScore, String word, DialogInterface.OnClickListener onDialogClose);

        void showGameOverDialog(long score, String word, DialogInterface.OnClickListener onDialogClose);

        ContentResolver getContentResolver();

        void animateImageView(int index, Animation.AnimationListener listener);

        void sendUpdateScoreBroadCast(long newScore);

        void publishScore(long score);
    }

    interface GamePresenter extends BasePresenter {
        GameActivity getGameActivity();

        void setGameActivity(GameActivity gameActivity);

        @Bindable
        String getLevel();
        @Bindable
        String getScore();

        void setGameStatus(GameStatus gameStatus);

        @Bindable
        Boolean[] getLifes();
        @Bindable
        String getDefinition();
        @Bindable
        String getHiddenWord();
        @Bindable
        String getGuess();

        void setGuess(String guess);

        void resetGame();

        void initGame(WordEntry wordEntry);

        void onGuessClick();

        void saveState(ContentResolver contentResolver, AbstractDaoInterface.OnTaskFinishListener<GameStatusEntity> onTaskFinishListener);

        GameStatus getGameStatus();
    }
}
