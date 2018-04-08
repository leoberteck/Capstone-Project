package com.leoberteck.wtw;

import com.leoberteck.wtw.entities.GameStatus;
import com.leoberteck.wtw.entities.GameStatusImpl;
import com.leoberteck.wtw.exceptions.GameOverException;

import java.util.ArrayList;
import java.util.Random;

public class WhatTheWord {

    public static final int INITIAL_HEARTS = 5;
    public static final int MAX_SCORE = 10000;
    private static final Random rand = new Random();

    /**
     * Instantiates a new GameStatus based on a previous game session
     * @param word the hidden word
     * @param definition definition of the hidden word
     * @param lastGame the last game session
     * @return a new game session
     */
    public GameStatus startNewGame(
            String word
            , String definition
            , GameStatus lastGame){
        GameStatus gameStatus = new GameStatusImpl();
        gameStatus.setWord(word);
        gameStatus.setDefinition(definition);
        gameStatus.setHearts(INITIAL_HEARTS);
        gameStatus.setRevealedLetters(new ArrayList<Integer>());
        gameStatus.setScore(lastGame != null ? lastGame.getScore() : 0L);
        gameStatus.setBeatenWords(lastGame != null ? lastGame.getBeatenWords() : new ArrayList<Integer>());
        return gameStatus;
    }

    /**
     No wrong guesses : 10000 points
     1 wrong guess : 5000 points
     2 wrong guesses : 2500 points
     3 wrong guesses: 1250 points
     4 wrong guesses: 625 points
     two elevated by the number of wrong guesses the user had divided by 100000
     * @param gameStatus
     * @return The score based on wrong guesses
     */
    public long calculateGameScore(GameStatus gameStatus){
        long score = MAX_SCORE;
        int divider = (int)Math.pow(2, INITIAL_HEARTS - gameStatus.getHearts());
        score = score / divider;
        return score;
    }


    /**
     * Updates the game status when the user makes a wrong guess.
     * If the user has used his last change to guess the word, throws a
     * GameOverEception, else, it reveals a new letter of the random word
     * and returns the index of the revealed letter.
     * @param gameStatus the current game
     * @return the index of the revealed letter.
     * @throws GameOverException
     */
    public int calculateWrongGuess(GameStatus gameStatus) throws GameOverException{
        gameStatus.setHearts(gameStatus.getHearts() - 1);
        if(gameStatus.getHearts() == 0){
            throw new GameOverException();
        }
        String word = gameStatus.getWord();
        int letterIndex = rand.nextInt(word.length());
        gameStatus.getRevealedLetters().add(letterIndex);
        return letterIndex;
    }


}