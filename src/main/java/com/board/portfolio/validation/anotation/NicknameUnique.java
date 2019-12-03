package com.board.portfolio.validation.anotation;

import com.board.portfolio.validation.validator.constraint.NicknameUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NicknameUniqueValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NicknameUnique {
    String message() default "Nickname is exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
