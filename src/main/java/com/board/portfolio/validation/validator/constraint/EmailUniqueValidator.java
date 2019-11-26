package com.board.portfolio.validation.validator.constraint;

import com.board.portfolio.repository.AccountRepository;
import com.board.portfolio.validation.anotation.EmailUnique;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void initialize(EmailUnique emailUnique) {
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        boolean isExistEmail = accountRepository.existsByEmail(email);
        return !isExistEmail;
    }
}