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
public class JwtUserAccount extends DefaultUserAccount {

    private static final long serialVersionUID = 1L;

    private String token;

    private boolean tokenCorrect;
    
}
