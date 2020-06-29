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
package com.youkol.support.shiro.authc.pam;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.ShortCircuitIterationException;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom implementation inherited {@link ModularRealmAuthenticator}
 * Store the last AuthenticationException for multi realm.
 * Fix when use multi realm, lost the AuthenticationException info. Add by jackiea
 * 
 * @author jackiea
 * @see ModularRealmAuthenticator
 */
public class MultiRealmAuthenticator extends ModularRealmAuthenticator {
    
    private static final Logger log = LoggerFactory.getLogger(MultiRealmAuthenticator.class);

    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {

        AuthenticationStrategy strategy = getAuthenticationStrategy();

        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        // Store AuthenticationException for multi realm.
        // Fix when use multi realm, lost the AuthenticationException info. Add by jackiea
        AuthenticationException authenticationException = null;

        for (Realm realm : realms) {

            try {
                aggregate = strategy.beforeAttempt(realm, token, aggregate);
            } catch (ShortCircuitIterationException shortCircuitSignal) {
                // Break from continuing with subsequnet realms on receiving 
                // short circuit signal from strategy
                break;
            }

            if (realm.supports(token)) {

                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

                AuthenticationInfo info = null;
                Throwable t = null;
                try {
                    info = realm.getAuthenticationInfo(token);
                } catch (Throwable throwable) {
                    // Store AuthenticationException
                    if (throwable instanceof AuthenticationException) {
                        authenticationException = (AuthenticationException) throwable;
                    }

                    t = throwable;
                    if (log.isDebugEnabled()) {
                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                        log.debug(msg, t);
                    }
                }

                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);

            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }

        // Fix when use multi realm, lost the AuthenticationException info. Add by jackiea
        try {
            aggregate = strategy.afterAllAttempts(token, aggregate);
        } catch (AuthenticationException ex) {
            // could not be authenticated by any configured realms
            if (authenticationException != null) {
                log.debug("Could not be authenticated by any configured realms", ex);
                throw authenticationException;
            }

            throw ex;
        }
        

        return aggregate;
    }
}
