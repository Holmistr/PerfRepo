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
package org.perfrepo.web.dao;

import org.perfrepo.web.model.TestExecution;
import org.perfrepo.web.model.TestExecutionParameter;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for {@link TestExecutionParameter}
 *
 * @author Pavel Drozd (pdrozd@redhat.com)
 * @author Michal Linhard (mlinhard@redhat.com)
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class TestExecutionParameterDAO extends DAO<TestExecutionParameter, Long> {

   /**
    * TODO: document this
    *
    * @param testExecution
    * @param parameter
    * @return
    */
   public TestExecutionParameter getParameterByNameAndExecution(TestExecution testExecution, TestExecutionParameter parameter) {
      CriteriaBuilder cb = criteriaBuilder();
      CriteriaQuery<TestExecutionParameter> criteria = cb.createQuery(TestExecutionParameter.class);

      Root<TestExecutionParameter> rParam = criteria.from(TestExecutionParameter.class);
      Join<TestExecutionParameter, TestExecution> rTestExecution = rParam.join("testExecution");

      Predicate pFixedTest = cb.equal(rTestExecution.get("id"), cb.parameter(Long.class, "testExecutionId"));
      Predicate pFixedName = cb.equal(rParam.get("name"), cb.parameter(String.class, "paramName"));

      criteria.where(cb.and(pFixedTest, pFixedName));
      criteria.select(rParam);

      TypedQuery<TestExecutionParameter> query = query(criteria);
      query.setParameter("testExecutionId", testExecution.getId());
      query.setParameter("paramName", parameter.getName());
      List<TestExecutionParameter> params = query.getResultList();

      if (params.isEmpty()) {
         return null;
      }

      return params.get(0);
   }

   /**
    * Finds all the test execution parameters associated with specified test execution and
    * parameter name provided. In other words, it allows to search by test execution parameters.
    *
    * @param execIdList
    * @param paramNameList
    * @return
    */
   public List<TestExecutionParameter> find(List<Long> execIdList, List<String> paramNameList) {
      CriteriaBuilder cb = criteriaBuilder();
      CriteriaQuery<TestExecutionParameter> criteria = cb.createQuery(TestExecutionParameter.class);

      Root<TestExecutionParameter> rParam = criteria.from(TestExecutionParameter.class);

      Predicate pParamNameInList = rParam.get("name").in(cb.parameter(List.class, "paramNameList"));
      Predicate pExecIdInList = rParam.get("testExecution").get("id").in(cb.parameter(List.class, "execIdList"));

      rParam.fetch("testExecution");

      criteria.where(cb.and(pParamNameInList, pExecIdInList));
      criteria.select(rParam);

      TypedQuery<TestExecutionParameter> query = query(criteria);
      query.setParameter("paramNameList", paramNameList);
      query.setParameter("execIdList", execIdList);

      return query.getResultList();
   }

   /**
    * Finds all test execution parameters with matching prefix
    *
    * @param prefix
    * @return
    */
   public List<TestExecutionParameter> findByPrefix(String prefix) {
      CriteriaBuilder cb = criteriaBuilder();
      CriteriaQuery<TestExecutionParameter> criteria = cb.createQuery(TestExecutionParameter.class);

      Root<TestExecutionParameter> testExecutionParameter = criteria.from(TestExecutionParameter.class);

      criteria.select(testExecutionParameter);
      criteria.where(cb.like(cb.lower(testExecutionParameter.<String>get("name")), prefix.toLowerCase() + "%"));

      return query(criteria).getResultList();
   }

   public List<TestExecutionParameter> findByExecution(Long testExecutionId) {
      Map<String, Object> params = new HashMap<>();
      params.put("executionId", testExecutionId);
      return findByNamedQuery(TestExecutionParameter.FIND_BY_TEST_EXECUTION, params);
   }
}