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

import org.perfrepo.web.model.Metric;
import org.perfrepo.web.model.Test;
import org.perfrepo.web.model.to.SearchResultWrapper;
import org.perfrepo.web.model.user.User;
import org.perfrepo.web.service.search.TestSearchCriteria;
import org.perfrepo.web.service.validation.annotation.ValidMetric;
import org.perfrepo.web.service.validation.annotation.ValidTest;

import java.util.List;
import java.util.Set;

import static org.perfrepo.web.service.validation.ValidationType.DUPLICATE_CHECK;
import static org.perfrepo.web.service.validation.ValidationType.EXISTS;
import static org.perfrepo.web.service.validation.ValidationType.ID_NULL;
import static org.perfrepo.web.service.validation.ValidationType.SEMANTIC_CHECK;

/**
 * Service for tests
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public interface TestService {

   /******** Methods related to test ********/

   /**
    * Create a new test with collection of metrics.
    *
    * @param test
    * @return
     */
   Test createTest(@ValidTest(type = {ID_NULL, SEMANTIC_CHECK, DUPLICATE_CHECK}) Test test);

   /**
    * Updates the test.
    *
    * @param test
    * @return
     */
   Test updateTest(@ValidTest(type = {EXISTS, SEMANTIC_CHECK, DUPLICATE_CHECK}) Test test);

   /**
    * Delete a test with all it's sub-objects, but first it disassociates all the metrics from it.
    * USE WITH CAUTION!!!
    *
    * @param test
    */
   void removeTest(@ValidTest Test test);

   /**
    * Get test.
    *
    * @param id Test id
    * @return Test
    */
   Test getTest(Long id);

   /**
    * Get test.
    *
    * @param uid test uid
    * @return Test
    */
   Test getTest(String uid);

   /**
    * Returns all tests.
    *
    * @return tests
    */
   List<Test> getAllTests();

   /**
    * @return list of all test belonging to one of the groups current logged user is in.
    */
   SearchResultWrapper<Test> getTestsForUser(User user);

   /**
    * Returns list of Tests according to criteria defined by TestSearchCriteria
    *
    * @param search
    * @return result
    */
   SearchResultWrapper<Test> searchTests(TestSearchCriteria search);

   /**
    * Returns test uids matching prefix
    *
    * @param prefix
    * @return test prefixes
    */
   List<Test> getTestsByUidPrefix(String prefix);

   /******** Methods related to metric ********/

   /**
    * Adds metric to test. If the metric already exists, it assigns the existing entity. If it doesn't,
    * it creates the entity and then assigns it to test.
    *
    * @param metric
    * @param test
    * @return metric
    */
   Metric addMetric(@ValidMetric(type = {SEMANTIC_CHECK}) Metric metric, @ValidTest Test test);

   /**
    * Update metric.
    *
    * @param metric
    * @return Updated metric
    */
   Metric updateMetric(@ValidMetric(type = {EXISTS, SEMANTIC_CHECK, DUPLICATE_CHECK}) Metric metric);

   /**
    * Disassociate metric from the test. If there are no more tests associated with the metric,
    * the metric is automatically removed.
    *
    * @param metric
    * @param test
    */
   void removeMetricFromTest(@ValidMetric Metric metric, @ValidTest Test test);

   /**
    * Retrieves metric.
    *
    * @param id
    * @return metric
    */
   Metric getMetric(Long id);

    /**
     * Retrieves all metrics.
     *
     * @return
     */
   List<Metric> getAllMetrics();

   /**
    * Returns all metrics, which are defined on the Test
    *
    * @return metrics
    */
   Set<Metric> getMetricsForTest(Test test);

   /******** Methods related to subscribers ********/

   /**
    * Adds current user to the subscriber list of the given test.
    *
    * @param test
    */
   void addSubscriber(@ValidTest Test test);

   /**
    * Removes current user from the subscriber list of the given test
    *
    * @param test
    */
   void removeSubscriber(@ValidTest Test test);

   /**
    * Returns true if the given user is subscribed to given test
    *
    * @param user
    * @param test
    * @return boolean
    */
   boolean isUserSubscribed(User user, @ValidTest Test test);

   /**
    * Returns true if the current user is subscribed to given test
    *
    * @param test
    * @return boolean
    */
   boolean isUserSubscribed(@ValidTest Test test);

}
