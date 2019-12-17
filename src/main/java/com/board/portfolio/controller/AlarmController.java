package com.board.portfolio.controller;

import com.board.portfolio.security.account.AccountSecurityDTO;
import com.board.portfolio.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class AlarmController {

    private AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService){
        this.alarmService = alarmService;
    }

    @GetMapping("/alarm")
    public ResponseEntity getAlarmList(@ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        return ResponseEntity.ok(alarmService.getAlarmList(accountDTO));
    }
    @PutMapping("/alarm/{alarmId}")
    public ResponseEntity checkAlarm(@PathVariable String alarmId,
                                     @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        alarmService.checkAlarm(alarmId, accountDTO);
        return ResponseEntity.ok(Result.SUCCESS);
    }
    @DeleteMapping("/alarm/{alarmId}")
    public ResponseEntity deleteAlarm(@PathVariable String alarmId,
                                      @ModelAttribute("accountDTO") AccountSecurityDTO accountDTO){
        alarmService.deleteAlarm(alarmId, accountDTO);
        return ResponseEntity.ok(Result.SUCCESS);
    }
}
