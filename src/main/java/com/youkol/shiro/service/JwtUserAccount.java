package com.youkol.shiro.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author jackiea
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class JwtUserAccount implements UserAccount {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String username;

    private String password;

    private String token;

    private String status;

    private boolean credentialsCorrect;

    private boolean credentialsExpired;
    
}
