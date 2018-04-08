package com.leoberteck.wtw.entities;

import java.util.List;

public class GameStatusImpl implements GameStatus {

    private Long score;
    private String definition;
    private String word;
    private List<Integer> revealedLetters;
    private Integer hearts;
    private List<Integer> beatenWords;

    @Override
    public long getScore() {
        return score;
    }

    @Override
    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public List<Integer> getRevealedLetters() {
        return revealedLetters;
    }

    @Override
    public void setRevealedLetters(List<Integer> revealedLetters) {
        this.revealedLetters = revealedLetters;
    }

    @Override
    public int getHearts() {
        return hearts;
    }

    @Override
    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    @Override
    public List<Integer> getBeatenWords() {
        return beatenWords;
    }

    @Override
    public void setBeatenWords(List<Integer> beatenWords) {
        this.beatenWords = beatenWords;
    }
}
