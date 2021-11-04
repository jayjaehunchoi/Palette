package com.palette.exception;

import org.springframework.http.HttpStatus;

public class CommentException extends PaletteException{

    public CommentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
