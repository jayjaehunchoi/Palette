package com.palette.exception;
import org.springframework.http.HttpStatus;

public class MemberException extends PaletteException {

    public MemberException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
