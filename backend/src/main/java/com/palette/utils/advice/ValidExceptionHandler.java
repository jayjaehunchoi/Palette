package com.palette.utils.advice;

import com.palette.dto.GeneralResponse;
import com.palette.dto.response.ValidErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validErrorHandler(MethodArgumentNotValidException e){
        log.error("[validErrorHandler]", e);
        BindingResult bindingResult = e.getBindingResult();
        List<ValidErrorResponseDto> validErrorDtos = createValidErrorDtos(bindingResult);
        return ResponseEntity.badRequest().body(GeneralResponse.builder().error(validErrorDtos).build());
    }

    private List<ValidErrorResponseDto>  createValidErrorDtos(BindingResult bindingResult) {
        List<ValidErrorResponseDto> dtos = new ArrayList<>();
        if(bindingResult.hasErrors()){
            addValidateError(bindingResult, dtos);
        }
        return dtos;
    }

    private void addValidateError(BindingResult bindingResult, List<ValidErrorResponseDto> dtos) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError allError : allErrors) {
            FieldError fieldError = (FieldError) allError;
            ValidErrorResponseDto validErrorResponseDto = new ValidErrorResponseDto(fieldError.getField(), fieldError.getDefaultMessage());
            dtos.add(validErrorResponseDto);
        }
    }
}
