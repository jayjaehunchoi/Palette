package com.palette.exception;

import org.springframework.http.HttpStatus;

public class BudgetException extends PaletteException{
    public BudgetException(String message){super(message, HttpStatus.BAD_REQUEST);}
}
