package com.palette.exception;

import org.springframework.http.HttpStatus;

// 부모 Exception으로 사용하세요!
public class PaletteException extends RuntimeException{
    private final HttpStatus httpStatus;

    public PaletteException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
