package com.leoberteck.whattheword.data.entities;

public class ScoreEntity implements BaseEntity {

    private Long id;
    private Long score;

    public ScoreEntity() {
    }

    public ScoreEntity(Long score) {
        this.score = score;
    }

    @Override
    public Long getId() {
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
