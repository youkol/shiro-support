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
package com.youkol.support.shiro.authc.credential;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;

/**
 * NOTE: This is only a temporary solution, please decide for yourself whether to use.
 *
 * Adjust PasswordMatcher to make it compatible DefaultPasswordService.
 *
 * @author jackiea
 * @see PasswordMatcher
 * @see DefaultPasswordService
 */
public class SimplePasswordMatcher extends PasswordMatcher {

    @Override
    protected Object getStoredPassword(AuthenticationInfo storedAccountInfo) {
        if (!(storedAccountInfo instanceof SaltedAuthenticationInfo)) {
            return super.getStoredPassword(storedAccountInfo);
        }
        PasswordService passwordService = this.getPasswordService();
        if (!(passwordService instanceof DefaultPasswordService)) {
            return super.getStoredPassword(storedAccountInfo);
        }

        DefaultPasswordService defaultPasswordService = (DefaultPasswordService) passwordService;
        HashService hashService = defaultPasswordService.getHashService();
        if (!(passwordService instanceof DefaultHashService)) {
            return super.getStoredPassword(storedAccountInfo);
        }

        DefaultHashService hashingService = (DefaultHashService) hashService;
        int iterations = hashingService.getHashIterations();
        String algorithmName = hashingService.getHashAlgorithmName();

        SaltedAuthenticationInfo info = (SaltedAuthenticationInfo) storedAccountInfo;

        HashRequest request = new HashRequest.Builder()
            .setSource(info.getCredentials())
            .setAlgorithmName(algorithmName)
            .setSalt(info.getCredentialsSalt())
            .setIterations(iterations)
            .build();

        return hashingService.computeHash(request);
    }

}
