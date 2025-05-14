package com.colvir.bootcamp.homework5.exception;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(String message) {
        super(message);
    }
}
