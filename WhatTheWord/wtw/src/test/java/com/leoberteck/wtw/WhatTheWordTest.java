package com.leoberteck.wtw;

import com.leoberteck.wtw.entities.GameStatus;
import com.leoberteck.wtw.entities.GameStatusImpl;
import com.leoberteck.wtw.exceptions.GameOverException;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class WhatTheWordTest {

    private final WhatTheWord whatTheWord = new WhatTheWord();

    @Test
    public void startNewGame_NewGame() {
        final String word = "Potato";
        final String definition = "Something I love <3";
        GameStatus gameStatus = whatTheWord.startNewGame(
            word, definition, null);
        Assert.assertEquals(0, gameStatus.getScore());
        Assert.assertEquals(word, gameStatus.getWord());
        Assert.assertEquals(definition, gameStatus.getDefinition());
        Assert.assertEquals(WhatTheWord.INITIAL_HEARTS, gameStatus.getHearts());
        Assert.assertNotNull(gameStatus.getBeatenWords());
        Assert.assertEquals(0, gameStatus.getBeatenWords().size());
        Assert.assertNotNull(gameStatus.getRevealedLetters());
        Assert.assertEquals(0, gameStatus.getRevealedLetters().size());
    }

    @Test
    public void startNewGame_FromPreviousGame() {

        final String word = "Potato";
        final String definition = "Something I love <3";
        final long previousScore = 123456;
        final Long beatenWordId = 999L;

        GameStatus previous = new GameStatusImpl();
        previous.setId(beatenWordId);
        previous.setScore(previousScore);
        previous.setBeatenWords(new ArrayList<Long>());

        GameStatus gameStatus = whatTheWord.startNewGame(word, definition, previous);
        Assert.assertEquals(previousScore, gameStatus.getScore());
        Assert.assertEquals(word, gameStatus.getWord());
        Assert.assertEquals(definition, gameStatus.getDefinition());
        Assert.assertEquals(WhatTheWord.INITIAL_HEARTS, gameStatus.getHearts());
        Assert.assertNotNull(gameStatus.getBeatenWords());
        Assert.assertEquals(1, gameStatus.getBeatenWords().size());
        Assert.assertEquals(beatenWordId, gameStatus.getBeatenWords().get(0));
        Assert.assertNotNull(gameStatus.getRevealedLetters());
        Assert.assertEquals(0, gameStatus.getRevealedLetters().size());
    }

    @Test
    public void calculateGameScore_FullScore() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(5);
        gameStatus.setScore(0);
        whatTheWord.calculateRightGuess(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE, gameStatus.getScore());
    }

    @Test
    public void calculateGameScore_OneWrongGues() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(4);
        gameStatus.setScore(0);
        whatTheWord.calculateRightGuess(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/2, gameStatus.getScore());
    }

    @Test
    public void calculateGameScore_TwoWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(3);
        gameStatus.setScore(0);
        whatTheWord.calculateRightGuess(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/4, gameStatus.getScore());
    }

    @Test
    public void calculateGameScore_ThreeWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(2);
        gameStatus.setScore(0);
        whatTheWord.calculateRightGuess(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/8, gameStatus.getScore());
    }

    @Test
    public void calculateGameScore_FourWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(1);
        gameStatus.setScore(0);
        whatTheWord.calculateRightGuess(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/16, gameStatus.getScore());
    }

    @Test
    public void calculateWrongGuess() {
        final String word = "Potato";
        final String definition = "Something I love <3";

        GameStatus gameStatus = whatTheWord.startNewGame(word, definition, null);
        try {
            Integer index = whatTheWord.calculateWrongGuess(gameStatus);
            Assert.assertEquals(WhatTheWord.INITIAL_HEARTS-1, gameStatus.getHearts());
            Assert.assertEquals(1, gameStatus.getRevealedLetters().size());
            Assert.assertEquals(index, gameStatus.getRevealedLetters().get(0));
        } catch (GameOverException e) {
            Assert.fail("The game is not over yet");
        }
    }

    @Test
    public void calculateWrongGuess_GameOver() {
        final String word = "Potato";
        final String definition = "Something I love <3";

        GameStatus gameStatus = whatTheWord.startNewGame(word, definition, null);
        try {
            for (int x=0;x<5;x++){
                whatTheWord.calculateWrongGuess(gameStatus);
            }
        } catch (GameOverException e) {
            Assert.assertEquals(0, gameStatus.getHearts());
            Assert.assertEquals(WhatTheWord.INITIAL_HEARTS-1, gameStatus.getRevealedLetters().size());
            return;
        }
        Assert.fail();
    }
}