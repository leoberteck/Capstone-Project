package com.leoberteck.whattheword.entities;

import com.leoberteck.wtw.entities.GameStatus;
import com.leoberteck.wtw.entities.GameStatusImpl;

public class GameStatusEntity extends GameStatusImpl implements GameStatus, BaseEntity {

    private Long id;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

}