package com.board.portfolio.repository;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface AlarmRepository extends JpaRepository<Alarm,String> {

    List<Alarm> findAllByTargetAccountOrderByRecieveDateDesc(Account account);
}
