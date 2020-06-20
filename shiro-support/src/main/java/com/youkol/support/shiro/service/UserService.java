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

import org.apache.shiro.authc.UnknownAccountException;

/**
 * @author jackiea
 */
public interface UserService {

    /**
     *
     * @param username the username is the user-unique identity.
     * @return user record.
     * @throws UnknownAccountException when a principal that doesn't exist in the system
     */
    UserAccount findByUsername(String username) throws UnknownAccountException;
}
