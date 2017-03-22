//package org.perfrepo.web.service.reports.tablecomparison;
//
//import org.perfrepo.dto.report.table_comparison.ComparisonItemDto;
//import org.perfrepo.dto.report.table_comparison.GroupDto;
//import org.perfrepo.dto.report.table_comparison.TableComparisonReportDto;
//import org.perfrepo.dto.report.table_comparison.TableDto;
//import org.perfrepo.web.adapter.converter.PermissionConverter;
//import org.perfrepo.web.model.Metric;
//import org.perfrepo.web.model.Test;
//import org.perfrepo.web.model.TestExecution;
//import org.perfrepo.web.model.Value;
//import org.perfrepo.web.model.report.Report;
//import org.perfrepo.web.model.report.ReportProperty;
//import org.perfrepo.web.model.report.ReportType;
//import org.perfrepo.web.model.user.User;
//import org.perfrepo.web.dao.TestDAO;
//import org.perfrepo.web.dao.TestExecutionDAO;
//import org.perfrepo.web.service.ReportService;
//import org.perfrepo.web.service.TestService;
//import org.perfrepo.web.service.UserService;
//import org.perfrepo.web.session.UserSession;
//import org.perfrepo.web.util.ReportUtils;
//
//import javax.ejb.*;
//import javax.inject.Inject;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * Service bean for table comparison report. Contains all necessary methods to be able to work with table comparison reports.
// *
// * @author Jakub Markos (jmarkos@redhat.com)
// * @author Jiri Holusa (jholusa@redhat.com)
// */
//@Stateless
//@TransactionManagement(TransactionManagementType.CONTAINER)
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
//public class TableComparisonReportServiceBean {
//
//    @Inject
//    private TestDAO testDAO;
//
//    @Inject
//    private TestExecutionDAO testExecutionDAO;
//
//    @Inject
//    private UserService userService;
//
//    @Inject
//    private UserSession userSession;
//
//    @Inject
//    private ReportService reportService;
//
//    @Inject
//    private TestService testService;
//
//    @Inject
//    private PermissionConverter permissionConverter;
//
//    /**
//     * Creates new table comparison report
//     *
//     * @return ID of newly created report
//     */
//    public Long create(TableComparisonReportDto report) {
//        Report rawReport = new Report();
//        rawReport.setName(report.getName());
//        rawReport.setType(ReportType.valueOf(report.getTypeName()));
//        rawReport.setPermissions(permissionConverter.convertFromDtoToEntity(report.getPermissions()));
//
//        User user = userSession.getLoggedUser();
//        rawReport.setUser(user);
//
//        ReportUtils.createOrUpdateReportPropertyInMap(rawReport.getProperties(), "description", report.getDescription(), rawReport);
//
//        Map<String, ReportProperty> properties = storeTOIntoReportProperties(report.getGroups(), rawReport);
//        rawReport.setProperties(properties);
//
//        Report createdReport = reportService.createReport(rawReport);
//        return createdReport.getId();
//    }
//
//    /**
//     * Updates existing table comparison report.
//     *
//     * @param report
//     * @return ID of the updated report
//     */
//    public long update(TableComparisonReportDto report) {
//        Report rawReport = reportService.getReport(report.getId());
//        rawReport.setName(report.getName());
//        rawReport.setType(ReportType.valueOf(report.getTypeName()));
//        rawReport.setPermissions(permissionConverter.convertFromDtoToEntity(report.getPermissions()));
//
//        Map<String, ReportProperty> properties = storeTOIntoReportProperties(report.getGroups(), rawReport);
//        rawReport.setProperties(properties);
//
//        Report createdReport = reportService.updateReport(rawReport);
//        return createdReport.getId();
//    }
//
//    /**
//     * Loads all Group transfer objects of specified report from database and report properties.
//     *
//     * @param reportId
//     * @return pair of report name and list of groups
//     */
//    public TableComparisonReportDto load(Long reportId) {
//        Report report = reportService.getReport(reportId);
//        if (report == null) {
//            throw new IllegalArgumentException("Report with ID=" + reportId + " doesn't exist");
//        }
//
//        return loadTO(report);
//    }
//
//    /**
//     * This fills in the test executions of the item based on the ChooseOption of the comparison
//     *
//     * @param comparison
//     * @param comparisonItem
//     */
//    public void updateComparisonItem(Comparison comparison, ComparisonItem comparisonItem) {
//        // cleanup values from previous update
//        comparisonItem.setTestExecutions(null);
//        comparisonItem.setComparedExecutionId(null);
//        switch (comparison.getChooseOption()) {
//            case EXECUTION_ID:
//                long executionId = comparisonItem.getExecutionId();
//                TestExecution foundTestExecution = testService.getFullTestExecution(executionId);
//                if (foundTestExecution != null) {
//                    comparisonItem.addTestExecution(foundTestExecution);
//                }
//                break;
//            case SET_OF_TAGS:
//                if (comparisonItem.getTestId() == -1) {
//                    return;
//                }
//                Test test = testDAO.get(comparisonItem.getTestId());
//                TestExecutionSearchTO searchCriteria = new TestExecutionSearchTO();
//                searchCriteria.setTestUID(test.getUid());
//                searchCriteria.setTags(comparisonItem.getTags());
//                List<TestExecution> preliminaryResult = testExecutionDAO.searchTestExecutions(searchCriteria, userService.getLoggedUserGroupNames()).getResult();
//                List<Long> executionIds = preliminaryResult.stream().map(TestExecution::getId).collect(Collectors.toList());
//                List<TestExecution> result = testService.getFullTestExecutions(executionIds);
//                comparisonItem.setTestExecutions(result);
//                break;
//        }
//    }
//
//    public void updateComparison(Group group, Comparison comparison) {
//        Table dataTable = new Table();
//        ArrayList<TestExecution> comparedTestExecutions = new ArrayList<>();
//
//        switch (comparison.getSelectOption()) {
//            case LAST:
//                for (ComparisonItem comparisonItem : comparison.getComparisonItems()) {
//                    if (comparisonItem.getTestExecutions() != null && !comparisonItem.getTestExecutions().isEmpty()) {
//                        // sort by date
//                        comparisonItem.getTestExecutions().sort((o1, o2) -> o1.getStarted().compareTo(o2.getStarted()));
//                        // add the last one
//                        TestExecution executionToCompare = comparisonItem.getTestExecutions().get(comparisonItem.getTestExecutions().size() - 1);
//                        comparedTestExecutions.add(executionToCompare);
//                        comparisonItem.setComparedExecutionId(executionToCompare.getId());
//                        dataTable.addItem(comparisonItem); // we only compare items that have some test execution
//                    }
//                }
//                break;
//            case BEST:
//                throw new RuntimeException("unsupported");
//            case AVERAGE:
//                throw new RuntimeException("unsupported");
//        }
//
//        if (comparedTestExecutions.isEmpty()) {
//            comparison.setDataTable(null);
//            return;
//        }
//
//        // we only show metrics which all of the compared items have
//        List<Metric> commonMetrics = findCommonMetrics(comparedTestExecutions);
//
//        // find the baseline, if none, mark the first compared item
//        int baselineIndex = -1;
//        for (int i = 0; i < dataTable.getItems().size(); i++) {
//            if (dataTable.getItems().get(i).isBaseline()) {
//                baselineIndex = i;
//            }
//        }
//        if (baselineIndex == -1) {
//            comparison.setBaseline(dataTable.getItems().get(0));
//            baselineIndex = 0;
//        }
//
//        for (Metric commonMetric : commonMetrics) {
//            Table.Row row = new Table.Row();
//            row.setMetricName(commonMetric.getName());
//
//            double baselineValue = getValueByMetric(comparedTestExecutions.get(baselineIndex), commonMetric).getResultValue();
//
//            for (int i = 0; i < comparedTestExecutions.size(); i++) {
//                Table.Cell cell = new Table.Cell();
//                cell.setValue(getValueByMetric(comparedTestExecutions.get(i), commonMetric).getResultValue());
//                if (i == baselineIndex) {
//                    cell.setBaseline(true);
//                    cell.setDiffAgainstBaseline("0%");
//                    cell.setCellStyle(Table.CellStyle.NEUTRAL);
//                } else {
//                    cell.setBaseline(false);
//                    DecimalFormat FMT_PERCENT = new DecimalFormat("0.00");
//                    double diff = 0;
//                    switch (commonMetric.getComparator()) {
//                        case HB:
//                            diff = (cell.getValue() / baselineValue) * 100d - 100;
//                            break;
//                        case LB:
//                            diff = ((cell.getValue() / baselineValue) * 100d - 100) * -1;
//                            break;
//                    }
//                    cell.setDiffAgainstBaseline(FMT_PERCENT.format(diff) + " %");
//                    if (diff > 0) { // add a plus sign to positive difference
//                        cell.setDiffAgainstBaseline("+" + cell.getDiffAgainstBaseline());
//                    }
//                    cell.setCellStyle(getCellStyle(diff, group.getThreshold()));
//                }
//                row.addCell(cell);
//            }
//            dataTable.addRow(row);
//        }
//        comparison.setDataTable(dataTable);
//    }
//
//    private Value getValueByMetric(TestExecution testExecution, Metric metric) {
//        for (Value value : testExecution.getValues()) {
//            if (Objects.equals(value.getMetric().getId(), metric.getId())) {
//                return value;
//            }
//        }
//        return null;
//    }
//
//    private List<Metric> findCommonMetrics(List<TestExecution> testExecutions) {
//        ArrayList<Metric> commonMetrics = new ArrayList<>();
//        for (Value value : testExecutions.get(0).getValues()) {
//            Metric metric = value.getMetric(); // candidate for a common metric
//            boolean allHave = testExecutions.stream().allMatch(testExecution -> testExecution.getValues().stream().anyMatch(value1 -> Objects.equals(value1.getMetric().getId(), metric.getId())));
//            if (allHave) {
//                commonMetrics.add(metric);
//            }
//        }
//        return commonMetrics;
//    }
//
//    private Table.CellStyle getCellStyle(double difference, int threshold) {
//        if (difference > threshold) {
//            return Table.CellStyle.GOOD;
//        }
//        if (difference < threshold) {
//            return Table.CellStyle.BAD;
//        }
//        return Table.CellStyle.NEUTRAL;
//    }
//
//    /**
//     * Denormalizes all group transfer objects of single report into report properties.
//     *
//     * @param groups
//     * @param report
//     * @return
//     */
//    private Map<String, ReportProperty> storeTOIntoReportProperties(List<GroupDto> groups, Report report) {
//        Map<String, ReportProperty> properties = new HashMap<>();
//        for (int i = 0; i < groups.size(); i++) {
//            GroupDto group = groups.get(i);
//            String groupPrefix = "group" + i + ".";
//
//            ReportUtils.createOrUpdateReportPropertyInMap(properties, groupPrefix + "name", group.getName(), report);
//            ReportUtils.createOrUpdateReportPropertyInMap(properties, groupPrefix + "description", group.getDescription(), report);
//            ReportUtils.createOrUpdateReportPropertyInMap(properties, groupPrefix + "threshold", Integer.toString(group.getThreshold()), report);
//            for (int j = 0; j < group.getTables().size(); j++) {
//                TableDto comparison = group.getTables().get(j);
//                String comparisonPrefix = groupPrefix + "comparison" + j + ".";
//                storeTableProperties(properties, comparison, comparisonPrefix, report);
//            }
//        }
//
//        return properties;
//    }
//
//    /**
//     * Denormalizes Comparison transfer object properties into report properties
//     *
//     * @param properties
//     * @param table
//     * @param tablePrefix
//     * @param report
//     */
//    private void storeTableProperties(Map<String, ReportProperty> properties, TableDto table, String tablePrefix, Report report) {
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, tablePrefix + "name", table.getName(), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, tablePrefix + "description", table.getDescription(), report);
//
//        for (int i = 0; i < table.getItems().size(); i++) {
//            ComparisonItemDto comparisonItem = table.getItems().get(i);
//            String comparisonItemPrefix = tablePrefix + "item" + i + ".";
//            storeComparisonItem(properties, comparisonItem, comparisonItemPrefix, report);
//        }
//    }
//
//    /**
//     * Denormalizes ComparisonItem transfer object properties into report properties
//     *
//     * @param properties
//     * @param comparisonItem
//     * @param comparisonItemPrefix
//     * @param report
//     */
//    private void storeComparisonItem(Map<String, ReportProperty> properties, ComparisonItemDto comparisonItem, String comparisonItemPrefix, Report report) {
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "alias", comparisonItem.getAlias(), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "baseline", Boolean.toString(comparisonItem.isBaseline()), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "testId", comparisonItem.getTestId().toString(), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "executionId", comparisonItem.getExecutionId().toString(), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "tags", comparisonItem.getTagQuery(), report);
//        ReportUtils.createOrUpdateReportPropertyInMap(properties, comparisonItemPrefix + "parameters", comparisonItem.getParameterQuery(), report);
//    }
//
//    /**
//     * Recreates all groups transfer objects from report properties
//     *
//     * @param properties report properties
//     * @return
//     */
//    private List<Group> loadTOFromReportProperties(Map<String, ReportProperty> properties) {
//        List<Group> groups = new ArrayList<>();
//
//        int groupIndex = 0;
//        String groupPrefix = "group" + groupIndex + ".";
//        while (properties.containsKey(groupPrefix + "name")) {
//            Group group = new Group();
//
//            group.setName(properties.get(groupPrefix + "name").getValue());
//            ReportProperty groupDescription = properties.get(groupPrefix + "description");
//            if (groupDescription != null) {
//                group.setDescription(groupDescription.getValue());
//            }
//            group.setThreshold(Integer.parseInt(properties.get(groupPrefix + "threshold").getValue()));
//            int comparisonIndex = 0;
//            String comparisonPrefix = groupPrefix + "comparison" + comparisonIndex + ".";
//            while (properties.containsKey(comparisonPrefix + "name")) {
//                Comparison comparison = new Comparison();
//                comparison.setName(properties.get(comparisonPrefix + "name").getValue());
//                ReportProperty comparisonDescription = properties.get(comparisonPrefix + "description");
//                if (comparisonDescription != null) {
//                    comparison.setDescription(comparisonDescription.getValue());
//                }
//                comparison.setChooseOption(Comparison.ChooseOption.valueOf(properties.get(comparisonPrefix + "chooseBy").getValue()));
//
//                if (group.getComparisons() == null) {
//                    group.setComparisons(new ArrayList<>());
//                }
//
//                loadComparisonItems(comparisonPrefix, comparison, properties);
//
//                group.getComparisons().add(comparison);
//                comparisonIndex++;
//                comparisonPrefix = groupPrefix + "comparison" + comparisonIndex + ".";
//            }
//
//            groups.add(group);
//            groupIndex++;
//            groupPrefix = "group" + groupIndex + ".";
//        }
//
//        return groups;
//    }
//
//    /**
//     * Parses properties of comparison items of a single Comparison from report properties
//     *
//     * @param comparisonPrefix prefix of the comparison being processed
//     * @param comparison       comparison transfer object to be modified
//     * @param properties       report properties
//     */
//    private void loadComparisonItems(String comparisonPrefix, Comparison comparison, Map<String, ReportProperty> properties) {
//        int comparisonItemIndex = 0;
//        String comparisonItemPrefix = comparisonPrefix + "item" + comparisonItemIndex + ".";
//        while (properties.containsKey(comparisonItemPrefix + "alias")) {
//            ComparisonItem comparisonItem = new ComparisonItem();
//            comparisonItem.setAlias(properties.get(comparisonItemPrefix + "alias").getValue());
//            comparisonItem.setBaseline(Boolean.parseBoolean(properties.get(comparisonItemPrefix + "baseline").getValue()));
//            comparisonItem.setTestId(Long.parseLong(properties.get(comparisonItemPrefix + "testId").getValue()));
//            comparisonItem.setExecutionId(Long.parseLong(properties.get(comparisonItemPrefix + "executionId").getValue()));
//            ReportProperty tags = properties.get(comparisonItemPrefix + "tags");
//            if (tags != null) {
//                comparisonItem.setTags(tags.getValue());
//            }
//
//            comparison.addComparisonItem(comparisonItem);
//            comparisonItemIndex++;
//            comparisonItemPrefix = comparisonPrefix + "item" + comparisonItemIndex + ".";
//        }
//    }
//
//}
