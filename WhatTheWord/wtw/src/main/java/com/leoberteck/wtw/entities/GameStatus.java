package com.leoberteck.wtw.entities;

import java.util.List;

public interface GameStatus {
    long getScore();

    void setScore(long score);

    String getDefinition();

    void setDefinition(String definition);

    String getWord();

    void setWord(String word);

    List<Integer> getRevealedLetters();

    void setRevealedLetters(List<Integer> revealedLetters);

    int getHearts();

    void setHearts(int hearts);

    List<Integer> getBeatenWords();

    void setBeatenWords(List<Integer> beatenWords);
}
