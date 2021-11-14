package com.palette.exception;

import org.springframework.http.HttpStatus;

public class ExpenseException extends PaletteException{
    public ExpenseException(String message){super(message, HttpStatus.BAD_REQUEST);}
}
