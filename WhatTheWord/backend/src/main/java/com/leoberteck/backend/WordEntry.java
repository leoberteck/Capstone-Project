package com.leoberteck.backend;

import java.util.Objects;

public class WordEntry {
    private Integer _id;
    private String definition;
    private String word;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntry wordEntry = (WordEntry) o;
        return Objects.equals(get_id(), wordEntry.get_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id());
    }
}