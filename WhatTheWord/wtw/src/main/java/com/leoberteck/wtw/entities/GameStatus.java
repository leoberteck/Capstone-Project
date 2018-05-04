package com.leoberteck.wtw.entities;

import java.util.List;
import java.util.Set;

public interface GameStatus {

    Long getId();

    void setId(long id);

    int getLevel();

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

    Set<Long> getBeatenWords();

    void setBeatenWords(Set<Long> beatenWords);
}
