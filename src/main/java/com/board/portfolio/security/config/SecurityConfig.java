package com.board.portfolio.security.config;

import com.board.portfolio.security.filter.FilterSkipMatcher;
import com.board.portfolio.security.filter.JwtFilter;
import com.board.portfolio.security.handler.JwtFilterFailureHandler;
import com.board.portfolio.security.handler.JwtFilterSuccessHandler;
import com.board.portfolio.security.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtFilterSuccessHandler jwtFilterSuccessHandler;
    @Autowired
    JwtFilterFailureHandler jwtFilterFailureHandler;
    //filter 생성 및 Manager에 등록
    @Bean
    public JwtFilter jwtFilter() throws Exception {
        FilterSkipMatcher filterSkipMatcher = new FilterSkipMatcher(Arrays.asList("/api/account/signIn"), "/api/**");
        JwtFilter jwtFilter = new JwtFilter(filterSkipMatcher, jwtFilterSuccessHandler, jwtFilterFailureHandler);
        jwtFilter.setAuthenticationManager(super.authenticationManagerBean());
        return jwtFilter;
    }

    //Manager 등록
    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception{
        return super.authenticationManagerBean();
    }

    //Provider 등록
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.jwtProvider);
    }

    //Filter 등록
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable();

        http
                .headers().frameOptions().disable();

        http
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
