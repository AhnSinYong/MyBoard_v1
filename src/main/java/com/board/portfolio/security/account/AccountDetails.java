package com.board.portfolio.security.account;

import com.board.portfolio.domain.entity.Account;
import com.board.portfolio.domain.entity.AccountRole;
import com.board.portfolio.exception.BlankEmailException;
import com.board.portfolio.exception.BlankPasswordException;
import com.board.portfolio.exception.InvalidAuthAccountException;
import com.board.portfolio.exception.NotFoundEmailException;
import com.board.portfolio.security.token.SignInPostToken;
import com.board.portfolio.security.token.SignInPreToken;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDetails extends User {

    private AccountSecurityDTO account;

    private void saveAccount(Account account){
        this.account = new ModelMapper().map(account, AccountSecurityDTO.class);
    }

    public AccountDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static AccountDetails transFromAccountWithSave(Account account){
        AccountDetails detail = new AccountDetails(account.getEmail(), account.getPassword(), parseAuthorities(account.getRole()));
        detail.saveAccount(account);
        return detail;
    }
    private static AccountRole parseAccountRole(String roleName){
        return AccountRole.valueOf(roleName);
    }
    public static List<SimpleGrantedAuthority> parseAuthoritiesFromRoleName(String roleName){
        return  parseAuthorities(parseAccountRole(roleName));
    }
    private static List<SimpleGrantedAuthority> parseAuthorities(AccountRole role) {
        return Arrays.asList(role).stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    public void validatePreToken(SignInPreToken token,PasswordEncoder passwordEncoder){
        String email = token.getEmail();
        String password = token.getPassword();
        validateSignIn(email,password,passwordEncoder);
    }

    private void validateSignIn(String email, String password,PasswordEncoder passwordEncoder){
        if(!this.account.isAuth()){
            throw new InvalidAuthAccountException("please, authenticate");
        }
        if(email.equals("") || email == null){
            throw new BlankEmailException("please, enter \"email\"");
        }
        if(password.equals("") || password == null){
            throw new BlankPasswordException("please, enter \"password\"");
        }
        if(isAbleSignIn(email, password, passwordEncoder)){
            throw new NotFoundEmailException("please, check login info");
        }
    }

    private boolean isAbleSignIn(String email, String password, PasswordEncoder passwordEncoder){
        return !(isSameEmail(email)&&isSamePassword(password,passwordEncoder));
    }
    private boolean isSameEmail(String email){
        return email.equals(this.getEmail());
    }
    private boolean isSamePassword(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password, this.getPassword());
    }

    public String getEmail(){
        return (String) super.getUsername();
    }
    public String getPassword(){
        return (String) super.getPassword();
    }
    public AccountSecurityDTO getAccount(){
        return this.account;
    }

    public SignInPostToken getPostToken(AccountDetails details) {
        return new SignInPostToken(details.getAccount(), details.getPassword(), details.getAuthorities() );
    }
}
