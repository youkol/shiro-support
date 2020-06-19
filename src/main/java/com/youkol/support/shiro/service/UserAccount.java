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

import java.io.Serializable;
import java.util.Set;

/**
 * Provides user information.
 *
 * @author jackiea
 */
public interface UserAccount extends Serializable {

    /**
     * Returns user owned roles.
     *
     * @return Collection of user roles.
     */
    Set<String> getRoles();

    /**
     * Returns user owned permissions.
     *
     * @return Collection of user roles.
     */
    Set<String> getPermissions();

    /**
     * Returns the user id.
     *
     * @return the user id
     */
    String getUserId();

    /**
     * Returns the username.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Returns the password.
     *
     * @return the password
     */
    String getPassword();

    /**
     * Returns the password salt to encode password.
     *
     * @return the salt, if need
     */
    String getSalt();

    /**
     * Returns the user status.
     * ie: LOCKED, EXPIRED, ENABLED, DISABLED etc.
     *
     * @return the user status
     */
    String getStatus();

    /**
     * Indicates whether the user's credentials (password) has correct.
     * If you check the credentials has corrent in your owner service,
     * you must set this value for other service to used.
     *
     * @return <code>true</code>, if the user's credentials are correct.
     *         <code>false</code>, if not corrent.
     */
    boolean isCredentialsCorrect();

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return <code>true</code>, if the user's credentials are valid.
     *         <code>false</code>, if no longer valid.
     */
    boolean isCredentialsNonExpired();

    /**
     * Indicates whether the user's account has expired.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
	 *         <code>false</code> if no longer valid (ie expired)
     */
    boolean isAccountNonExpired();

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return <code>true</code>, if the user is not locked,
     *         <code>false</code> otherwise
     */
    boolean isAccountNonLocked();

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return <code>true</code> if the user is enabled,
     *         <code>false</code> otherwise
     */
    boolean isEnabled();
}
