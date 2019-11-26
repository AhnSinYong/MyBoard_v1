package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
