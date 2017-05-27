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
import org.perfrepo.web.model.Tag;
import org.perfrepo.web.model.TestExecution;
import org.perfrepo.web.model.TestExecutionAttachment;
import org.perfrepo.web.model.TestExecutionParameter;
import org.perfrepo.web.model.Value;
import org.perfrepo.web.model.to.SearchResultWrapper;
import org.perfrepo.web.service.search.TestExecutionSearchCriteria;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service for {@link TestExecution} entity.
 *
 * TODO: add validation
 * TODO: add authorization
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public interface TestExecutionService {

   /******** Methods related directly to test execution object ********/

   /**
    * Stores a new test execution.
    *
    * @param testExecution New test execution.
    * @return
    */
   TestExecution createTestExecution(TestExecution testExecution);

   /**
    * Updates test execution.
    * Update only attributes name, comment, started and collection of tags.
    * TODO: we should also update all the other entities
    *
    * @param updatedTestExecution
    * @return
    */
   TestExecution updateTestExecution(TestExecution updatedTestExecution);

   /**
    * Delete a test execution with all it's subobjects.
    *
    * @param testExecution
    */
   void removeTestExecution(TestExecution testExecution);

   /**
    * Get {@link TestExecution}.
    *
    * @param id
    * @return test execution
    */
   TestExecution getTestExecution(Long id);

   /**
    * Returns all test executions.
    *
    * @return List of all {@link TestExecution}
    */
   List<TestExecution> getAllTestExecutions();

   /**
    * Returns list of TestExecutions according to criteria defined by TestExecutionSearchCriteria
    *
    * @param search
    * @return result
    */
   SearchResultWrapper<TestExecution> searchTestExecutions(TestExecutionSearchCriteria search);

   /******** Methods related to test execution attachments ********/

   /**
    * Add attachment to the test execution.
    *
    * @param attachment
    * @return newly created attachment
    */
   TestExecutionAttachment addAttachment(TestExecutionAttachment attachment);

   /**
    * Delete attachment.
    *
    * @param attachment
    */
   void removeAttachment(TestExecutionAttachment attachment);

   /**
    * Get test execution attachment by id.
    *
    * @param id
    * @return attachment
    */
   TestExecutionAttachment getAttachment(Long id);

    /**
     * Get all attachments for test execution.
     *
     * @param testExecution
     * @return
     */
    List<TestExecutionAttachment> getAttachments(TestExecution testExecution);

   /******** Methods related to test execution parameters ********/

   /**
    * Adds test execution parameter
    *
    * @param parameter
    * @return
    */
   TestExecutionParameter addParameter(TestExecutionParameter parameter);

   /**
    * Updates test execution parameter
    *
    * @param parameter
    * @return test execution parameter
    */
   TestExecutionParameter updateParameter(TestExecutionParameter parameter);

    /**
     * Updates parameters all together. Adds new ones, modifies existing, deletes non-existing.
     * @param newParameters
     * @param testExecution
     */
   void updateParameters(Map<String, TestExecutionParameter> newParameters, TestExecution testExecution);

   /**
    * Removes TestExecutionParameter
    *
    * @param parameter
    */
   void removeParameter(TestExecutionParameter parameter);

   /**
    * Get parameter and test execution.
    *
    * @param id
    * @return test execution parameter
    */
   TestExecutionParameter getParameter(Long id);

   /**
    * Returns test execution parameters matching prefix.
    *
    * @param prefix
    * @return
    */
   List<TestExecutionParameter> getParametersByPrefix(String prefix);

    /**
     * Returns test executions parameters for test execution.
     *
     * @param testExecution
     * @return
     */
    List<TestExecutionParameter> getParameters(TestExecution testExecution);

   /******** Methods related to values ********/

   /**
    * Creates new value.
    *
    * @param value
    * @return value
    */
   Value addValue(Value value);

   /**
    * Updates Test Execution Value and the set of it's parameters.
    *
    * @param value
    * @return value
    */
   Value updateValue(Value value);

   /**
    * Removes value from TestExecution
    *
    * @param value
    */
   void removeValue(Value value);

    /**
     * Returns value.
     *
     * @param id
     */
    Value getValue(Long id);

    /**
     * Retrieves all values for test execution.
     *
     * @param testExecution
     * @return
     */
    List<Value> getValues(TestExecution testExecution);

    /**
     * Retrieves values for test execution and given metric.
     *
     * @param metric
     * @param testExecution
     */
    List<Value> getValues(Metric metric, TestExecution testExecution);

   /******** Methods related to tags ********/

   /**
    * Adds tag to test execution. If it's already existing, it assigns the existing one. If not, creates a new one.
    *
    * @param tag
    * @param testExecution
    * @return
    */
   Tag addTag(Tag tag, TestExecution testExecution);

   /**
    * Disassociate tag from the test execution. If there are no more test executions associated with the tag,
    * the tag is automatically removed.
    *
    * @param tag
    */
   void removeTagFromTestExecution(Tag tag, TestExecution testExecution);

    /**
     * Retrieves all tags associated with provided test execution.
     *
     * @param testExecution
     * @return
     */
    Set<Tag> getTags(TestExecution testExecution);

    /**
     * Probably not useful for real-world scenario, only for testing.
     *
     * @return
     */
    Set<Tag> getAllTags();

   /**
    * Returns tags matching prefix
    *
    * @param prefix
    * @return tag prefixes
    */
   Set<Tag> getTagsByPrefix(String prefix);

   /**
    * Perform mass operation. Adds tags to provided test executions.
    *
    * @param tags
    * @param testExecutions
    */
   void addTagsToTestExecutions(Set<Tag> tags, Collection<TestExecution> testExecutions);

   /**
    * Perform mass operation. Deletes tags from provided test executions.
    *
    * @param tags
    * @param testExecutions
    */
   void removeTagsFromTestExecutions(Set<Tag> tags, Collection<TestExecution> testExecutions);

}
