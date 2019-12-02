package com.board.portfolio.mail.manager;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.repository.AccountRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthMailManager implements Runnable{

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    ModelMapper modelMapper;

    private Long limitTime;
    @Getter
    private final List<AuthMail> authMailList = new ArrayList<>();
    @Getter
    private boolean isWork;

    @Autowired
    public AuthMailManager(@Value("${mail.auth.limit}") Long limit){
        this.limitTime = limit;
    }

    @PostConstruct
    private void init(){
        startManage();
    }
    private void startManage(){
        setAuthMailList();
        if(isHasItem(authMailList)){
            startNewThread();
        }
    }
    private boolean isHasItem(List list){
        return list.size()!=0;
    }
    private void startNewThread(){
        Thread thread = new Thread(this);
        thread.start();
    }

    private void setAuthMailList(){
        List<Account> accountList = accountRepository.findAllByIsAuthOrderBySignUpDateAsc(false);
        for(Account account : accountList){
            authMailList.add(parseFromAccount(account));
        }
    }

    public void startManage(AuthMail authMail){
        if(isWork){
            addAuthMailList(authMail);
            return;
        }
        startNewThread();
    }
    private void addAuthMailList(Account account){
        authMailList.add(parseFromAccount(account));
    }
    private void addAuthMailList(AuthMail authMail){
        authMailList.add(authMail);
    }

    private AuthMail parseFromAccount(Account account){
        return AuthMail.builder()
                .email(account.getEmail())
                .sendDate(account.getSignUpDate())
                .authKey(account.getAuthKey())
                .build();
    }

    @Override
    public void run() {
        this.isWork = true;
        while(true){
            int i=0;
            AuthMail authMail = authMailList.get(i);
            if(isExpire(authMail)){//인증메일 유효시간이 지났다면
                accountRepository.deleteById(authMail.getEmail());
                authMailList.remove(i);
                if(authMailList.size()==0)
                    break;
                continue;
            }
            try {//지나지 않았다면
                Thread.sleep(howLeave(authMail));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isWork = false;
    }

    private boolean isExpire(AuthMail authMail){
        long now = System.currentTimeMillis();
        long expireTime = this.limitTime +authMail.getSendDate().getTime();
        return now>expireTime;
    }
    private long howLeave(AuthMail authMail){
        long now = System.currentTimeMillis();
        long leaveTime = (this.limitTime +authMail.getSendDate().getTime()) - now;
        return leaveTime;
    }
}
