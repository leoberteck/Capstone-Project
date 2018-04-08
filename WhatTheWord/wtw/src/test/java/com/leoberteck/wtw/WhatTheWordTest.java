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
        final Integer beatenWordId = 999;

        GameStatus previous = new GameStatusImpl();
        previous.setScore(previousScore);
        previous.setBeatenWords(new ArrayList<Integer>());
        previous.getBeatenWords().add(beatenWordId);

        GameStatus gameStatus = whatTheWord.startNewGame(
                word, definition, previous);
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
        long score = whatTheWord.calculateGameScore(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE, score);
    }

    @Test
    public void calculateGameScore_OneWrongGues() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(4);
        long score = whatTheWord.calculateGameScore(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/2, score);
    }

    @Test
    public void calculateGameScore_TwoWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(3);
        long score = whatTheWord.calculateGameScore(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/4, score);
    }

    @Test
    public void calculateGameScore_ThreeWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(2);
        long score = whatTheWord.calculateGameScore(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/8, score);
    }

    @Test
    public void calculateGameScore_FourWrongGuesses() {
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setHearts(1);
        long score = whatTheWord.calculateGameScore(gameStatus);
        Assert.assertEquals(WhatTheWord.MAX_SCORE/16, score);
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