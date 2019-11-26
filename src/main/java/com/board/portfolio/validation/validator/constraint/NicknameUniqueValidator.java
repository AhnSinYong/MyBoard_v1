package com.board.portfolio.validation.validator.constraint;

import com.board.portfolio.repository.AccountRepository;
import com.board.portfolio.validation.anotation.NicknameUnique;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class NicknameUniqueValidator implements ConstraintValidator<NicknameUnique, String> {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public void initialize(NicknameUnique nicknameUnique) {
    }
    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext cxt) {
        boolean isExistEmail = accountRepository.existsByNickname(nickname);
        return !isExistEmail;
    }
}