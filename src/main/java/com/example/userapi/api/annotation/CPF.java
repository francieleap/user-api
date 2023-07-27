package com.example.userapi.api.annotation;

import com.example.userapi.api.validator.CPFValidador;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CPFValidador.class})
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF {
    String message() default "O CPF informado é invalido!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
