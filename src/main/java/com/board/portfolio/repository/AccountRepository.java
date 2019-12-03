package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findByEmailAndAuthKey(String email, String authKey);

    boolean existsByAuthKey(String authKey);

    boolean existsByEmailAndAuthKeyAndIsAuth(String email, String authKey, boolean isAuth);

    List<Account> findAllByIsAuthOrderBySignUpDateAsc(boolean isAuth);

    @Transactional
    void deleteByEmailAndIsAuth(String email, boolean isAuth);
}
