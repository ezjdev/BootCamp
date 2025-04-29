package com.colvir.bootcamp.homework5.exception;

public class NoSuchSongException extends RuntimeException {
    public NoSuchSongException(String message) {
        super(message);
    }
}
