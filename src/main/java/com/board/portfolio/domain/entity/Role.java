package com.board.portfolio.domain.entity;


import lombok.Getter;

@Getter
public enum Role {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private String roleName;
    Role(String roleName){
        this.roleName = roleName;
    }
}
