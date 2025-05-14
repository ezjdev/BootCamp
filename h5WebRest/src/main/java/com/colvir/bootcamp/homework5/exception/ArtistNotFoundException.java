package com.colvir.bootcamp.homework5.exception;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(String s) {
        super(s);
    }
}
