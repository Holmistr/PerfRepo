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
package org.perfrepo.web.model.report;

import org.perfrepo.enums.report.ReportType;
import org.perfrepo.web.model.Entity;
import org.perfrepo.web.model.user.User;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a report entity.
 *
 * @author Pavel Drozd (pdrozd@redhat.com)
 */
@javax.persistence.Entity
@Table(name = "report")
@NamedQueries({
        @NamedQuery(name = Report.GET_FAVORITE, query = "SELECT report FROM Report report, User user WHERE user.id = :userId AND report MEMBER OF user.favoriteReports")
})
public class Report implements Entity<Report>, Comparable<Report> {

   private static final long serialVersionUID = -2188625358440509257L;

   public static final String GET_FAVORITE = "Report.getFavoriteReports";

   @Id
   @SequenceGenerator(name = "REPORT_ID_GENERATOR", sequenceName = "REPORT_SEQUENCE", allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_ID_GENERATOR")
   private Long id;

   @Column(name = "name")
   @NotNull
   @Size(max = 255)
   private String name;

   @Column(name = "description")
   @Size(max = 4096)
   private String description;

   @Column(name = "type")
   @NotNull
   @Enumerated(EnumType.STRING)
   private ReportType type;

   @ManyToOne
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   @NotNull
   private User user;

   @OneToMany(mappedBy = "report")
   @MapKey(name = "name")
   private Map<String, ReportProperty> properties = new HashMap<>();

   @OneToMany(mappedBy = "report")
   private Set<Permission> permissions = new HashSet<>();

   // users that marked the report as favorite
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
      name = "favorite_report",
      joinColumns = {@JoinColumn(name = "report_id")},
      inverseJoinColumns = { @JoinColumn(name = "user_id")}
   )
   private Set<User> favorizers = new HashSet<>();

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public ReportType getType() {
      return type;
   }

   public void setType(ReportType type) {
      this.type = type;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public Map<String, ReportProperty> getProperties() {
      return properties;
   }

   public void setProperties(Map<String, ReportProperty> properties) {
      this.properties = properties;
   }

   public Set<Permission> getPermissions() {
      return permissions;
   }

   public void setPermissions(Set<Permission> permissions) {
      this.permissions = permissions;
   }

   public Set<User> getFavorizers() {
      return favorizers;
   }

   public void setFavorizers(Set<User> favorizers) {
      this.favorizers = favorizers;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Report)) return false;

      Report report = (Report) o;

      return getId() != null ? getId().equals(report.getId()) : report.getId() == null;
   }

   @Override
   public int hashCode() {
      return getId() != null ? getId().hashCode() : 0;
   }

   @Override
   public int compareTo(Report o) {
      return this.getName().compareTo(o.getName());
   }

   @Override
   public String toString() {
      return "Report{" +
              "id=" + id +
              ", name='" + name + '\'' +
              ", type='" + type + '\'' +
              '}';
   }
}