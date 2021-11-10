package com.palette.exception;

import org.springframework.http.HttpStatus;

public class GroupException extends PaletteException{
    public GroupException(String message){ super(message, HttpStatus.BAD_REQUEST);}
}
