/**
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.shiro.service;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author jackiea
 */
public interface UserService {

    UserAccount getUserAccount(String username);

    Set<String> getRoles(String username);

    Set<String> getPermissions(String username);

    Set<String> getPermissions(String username, Collection<String> roles);

    default String getSalt(UserAccount account) {
        String username = account.getUsername();
        String salt = account.getSalt();
        if (username == null) {
            throw new IllegalArgumentException("The username not allowed to be empty.");
        }
        String result = username;
        if (salt != null) {
            result += salt;
        }

        return result;
    }

    default String encryptPassword(String username, String password, String salt) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        if (salt != null) {
            credentialsSalt = ByteSource.Util.bytes(username + salt);
        }

        SimpleHash simpleHash = new SimpleHash(Md5Hash.ALGORITHM_NAME, password, credentialsSalt, 2);
        return simpleHash.toHex();
    }

    default boolean validatePassword(UserAccount storedAccount, String password) {
        String storedPassword = storedAccount.getPassword();
        String salt = getSalt(storedAccount);
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);

        SimpleHash currentHash = new SimpleHash(Md5Hash.ALGORITHM_NAME, password, credentialsSalt, 2);
        SimpleHash storedHash = new SimpleHash(Md5Hash.ALGORITHM_NAME);
        storedHash.setBytes(Hex.decode(storedPassword));

        return storedHash.equals(currentHash);
    }
}
