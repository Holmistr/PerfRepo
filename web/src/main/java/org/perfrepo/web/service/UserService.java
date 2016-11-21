/**
 * PerfRepo
 * <p>
 * Copyright (C) 2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.perfrepo.web.service;

import org.perfrepo.model.FavoriteParameter;
import org.perfrepo.model.Test;
import org.perfrepo.model.user.User;
import org.perfrepo.web.service.exceptions.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * TODO: document this
 */
public interface UserService {

   /**
    * Create new user. THE USER'S PASSWORD MUST BE IN PLAIN TEXT!
    *
    * @param user
    * @return user
    * @throws ServiceException
    */
   public User createUser(User user) throws ServiceException;

   /**
    * Updates user. THE USER'S PASSWORD MUST BE IN PLAIN TEXT!
    *
    * @param user
    * @return user
    * @throws ServiceException
    */
   public User updateUser(User user) throws ServiceException;

   /**
    * Deletes user
    *
    * @param user
    */
   public void removeUser(User user) throws ServiceException;

   /**
    * Retrieves managed entity of user
    *
    * @param id
    * @return user
    */
   public User getUser(Long id);

   /**
    * Return all information about user as detached entity (e.g. cloned)
    *
    * @param username
    * @return user
    */
   public User getUser(String username);

   /**
    * Retrieves all users
    *
    * @return
    */
   public List<User> getAllUsers();

   /**
    * Changes user password. Provides check if old password is equal to "current" password
    * and if yes then encrypts the new password and stores it.
    *
    * @param oldPassword
    * @param newPassword
    * @param user
    * @throws ServiceException
    */
   public void changePassword(String oldPassword, String newPassword, User user) throws ServiceException;

   /**
    * Returns all user properties
    *
    * @param user
    * @return user properties
    */
   public Map<String, String> getUserProperties(User user);

   /**
    * Updates user properties.
    *
    * @param properties
    * @param user
    */
   public void updateUserProperties(Map<String, String> properties, User user) throws ServiceException;

   /**
    * Adds favorite parameter of user to the test
    *
    * @param parameter
    * @param test
    * @param user
    */
   public void createFavoriteParameter(FavoriteParameter parameter, Test test, User user);

   /**
    * Updates favorite parameter of user to the test
    *
    * @param parameter
    * @param test
    * @param user
    */
   public void updateFavoriteParameter(FavoriteParameter parameter, Test test, User user);

   /**
    * Removes favorite parameter of the test from user
    *
    * @param parameter
    */
   public void removeFavoriteParameter(FavoriteParameter parameter);

   /**
    * Returns list of favorite parameters that has user selected to specific test
    *
    * @param test
    * @param user
    * @return list of favorite parameters
    */
   public List<FavoriteParameter> getFavoriteParametersForTest(Test test, User user);


}
