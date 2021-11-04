package com.palette.exception;

import org.springframework.http.HttpStatus;

public class PostException extends PaletteException{

    public PostException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
