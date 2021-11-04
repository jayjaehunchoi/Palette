package com.palette.exception;

import org.springframework.http.HttpStatus;

public class PostGroupException extends PaletteException{

    public PostGroupException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
