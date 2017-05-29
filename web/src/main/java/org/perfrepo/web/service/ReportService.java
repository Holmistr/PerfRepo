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

import org.perfrepo.web.model.report.Permission;
import org.perfrepo.web.model.report.Report;
import org.perfrepo.web.model.report.ReportProperty;
import org.perfrepo.web.model.to.SearchResultWrapper;
import org.perfrepo.web.service.search.ReportSearchCriteria;

import java.util.List;
import java.util.Set;

/**
 * Service layer for report in general. Provides general method, not specific to any type of report.
 *
 * TODO: add validation
 * TODO: add authorization
 *
 * @author Jiri Holusa <jholusa@redhat.com>
 */
public interface ReportService {

   /**
    * Create new report.
    *
    * @param report
    * @return
    */
   Report createReport(Report report);

   /**
    * Update existing report.
    *
    * @param report
    * @return
    */
   Report updateReport(Report report);

   /**
    * Removes report
    *
    * @param report
    */
   void removeReport(Report report);

   /**
    * Get report with all properties
    *
    * @param id
    * @return {@link Report} with all attributes fetched
    */
   Report getReport(Long id);

   /**
    * Get all reports for which exists any (READ, WRITE or PUBLIC) permission to logged user or user group.
    * @return List of {@link Report}
    */
   List<Report> getAllReports();

   /**
    * Searches through all reports based on criteria.
    *
    * @param criteria
    * @return
    */
   SearchResultWrapper<Report> searchReports(ReportSearchCriteria criteria);

    /**
     * Retrieves report properties.
     *
     * @param report
     * @return
     */
   List<ReportProperty> getReportProperties(Report report);

   /**
    * TODO: document this
    *
    * @return
    */
   List<Report> getFavoriteReports();

   /**
    * TODO: document this
    *
    * @param report
    */
   void markReportAsFavorite(Report report);

   /**
    * TODO: document this
    *
    * @param report
    */
   void unmarkReportAsFavorite(Report report);

   /******** Methods related to permissions ********/

   /**
    * Adds permission to provided report.
    *
    * @param permission
    */
   void addPermission(Permission permission);

   /**
    * Updates permission to provided report.
    *
    * @param permission
    */
   void updatePermission(Permission permission);

   /**
    * Deletes permission from provided report.
    *
    * @param permission
    */
   void deletePermission(Permission permission);

   /**
    * Returns all permission to report. If the permissions are not assigned, returns default permission.
    *
    * @param report
    * @return
    */
   Set<Permission> getReportPermissions(Report report);
}
