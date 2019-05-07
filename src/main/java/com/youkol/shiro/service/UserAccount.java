package com.youkol.shiro.service;

import java.io.Serializable;

/**
 * @author jackiea
 */
public interface UserAccount extends Serializable {

    String getUserId();

    String getUsername();

    String getPassword();

    String getSalt();

    String getStatus();

    boolean isCredentialsCorrect();

    boolean isCredentialsExpired();
}
