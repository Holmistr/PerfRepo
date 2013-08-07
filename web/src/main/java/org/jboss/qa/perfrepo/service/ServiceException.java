/* 
 * Copyright 2013 Red Hat, Inc.
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
package org.jboss.qa.perfrepo.service;

import javax.ejb.ApplicationException;

/**
 * Exception in service layer.
 * 
 * @author Michal Linhard (mlinhard@redhat.com)
 * 
 */
@ApplicationException(rollback = true)
public class ServiceException extends Exception {

   public interface Codes {
      static final int TEST_UID_EXISTS = 100;
      static final int TEST_EXECUTION_NOT_FOUND = 200;
      static final int METRIC_NOT_IN_TEST = 300;
      static final int METRIC_NOT_FOUND = 400;
      static final int METRIC_SHARING_ONLY_IN_GROUP = 500;
      static final int METRIC_EXISTS = 600;
      static final int TEST_NOT_FOUND = 700;
      static final int TEST_UID_NOT_FOUND = 800;
      static final int METRIC_HAS_VALUES = 900;
   }

   private int code;
   private Object[] params;

   public ServiceException(int code, Object[] params, String message) {
      super(message);
      this.code = code;
      this.params = params;
   }

   public Object[] getParams() {
      return params;
   }

   public int getCode() {
      return code;
   }
}