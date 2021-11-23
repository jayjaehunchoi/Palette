package com.palette.utils.validator;

import com.palette.utils.annotation.EnumNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class EnumNullValidator implements ConstraintValidator<EnumNull, Enum<?>> {

    @Override
    public void initialize(EnumNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if(value != null){
            return true;
        }
        return false;
    }
}
