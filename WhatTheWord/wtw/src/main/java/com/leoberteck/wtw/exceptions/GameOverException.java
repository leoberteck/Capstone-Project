package com.leoberteck.wtw.exceptions;

public class GameOverException extends Exception {
    public GameOverException() {
        super("The game is over");
    }
}
