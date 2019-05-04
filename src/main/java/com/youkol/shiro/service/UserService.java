package com.youkol.shiro.service;

import java.util.Collection;
import java.util.Set;

/**
 * @author jackiea
 */
public interface UserService {

    UserAccount getUserAccount(String username);

    Set<String> getRoles(String username);

    Set<String> getPermissions(String username);

    Set<String> getPermissions(String username, Collection<String> roles);
}
