package com.leoberteck.whattheword.entities;

public class Score implements BaseEntity {

    private Long id;
    private Long score;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
