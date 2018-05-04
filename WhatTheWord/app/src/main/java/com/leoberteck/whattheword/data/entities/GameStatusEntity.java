package com.leoberteck.whattheword.data.entities;

import com.leoberteck.wtw.entities.GameStatus;
import com.leoberteck.wtw.entities.GameStatusImpl;

public class GameStatusEntity extends GameStatusImpl implements GameStatus, BaseEntity {

    public GameStatusEntity(){}

    public GameStatusEntity(GameStatus gameStatus) {
        setId(gameStatus.getId());
        setHearts(gameStatus.getHearts());
        setWord(gameStatus.getWord());
        setDefinition(gameStatus.getDefinition());
        setScore(gameStatus.getScore());
        setRevealedLetters(gameStatus.getRevealedLetters());
        setBeatenWords(gameStatus.getBeatenWords());
    }
}