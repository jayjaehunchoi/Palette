package com.palette.utils.advice;

import com.palette.dto.GeneralResponse;
import com.palette.exception.PaletteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice
public class PaletteExceptionHandler {

    // Palette Exception simple Handler
    @ExceptionHandler(PaletteException.class)
    public ResponseEntity<?> paletteExHandler(PaletteException e){
      log.error("에러 발생 {}",e.getMessage());
        GeneralResponse<Object> dto = GeneralResponse.builder().error(e.getMessage()).build();
        return ResponseEntity.badRequest().body(dto);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpStatusCodeException.class)
    public GeneralResponse httpStatusCodeError(HttpStatusCodeException e){
        return GeneralResponse.builder().error(e.getMessage()).build();
    }
}
