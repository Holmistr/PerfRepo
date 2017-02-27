(function() {
    'use strict';

    angular
        .module('org.perfrepo.testExecution.detail.value')
        .component('testExecutionMultiValueDataTableView', {
            bindings: {
                executionValuesGroup: '<',
                testExecutionId: '<',
                metric: '<',
                onUpdateTable: '&'
            },
            controller: TestExecutionMultiValueDataTableViewController,
            controllerAs: 'vm',
            templateUrl: 'app/test_execution/components/multi_value_data_table_view/multi-value-data-table-view.view.html'
        });

    function TestExecutionMultiValueDataTableViewController(testExecutionValueModalService, testExecutionService,
                                                            $filter) {
        var vm = this;
        vm.addValueObject = addValueObject;
        vm.showChart = showChart;
        vm.editValueObject = editValueObject;
        vm.deleteValueObject = deleteValueObject;
        vm.getValueObjectParameter = getValueObjectParameter;

        function showChart(parameterName) {
            testExecutionValueModalService.showChart(vm.executionValuesGroup.values, parameterName, vm.metric.name);
        }

        function addValueObject() {
            var modalInstance = testExecutionValueModalService.createValue([vm.executionValuesGroup],
                [vm.metric], vm.testExecutionId, true);

            modalInstance.result.then(function () {
                updateMultiValueTable();
            });
        }

        function editValueObject(index) {
            var modalInstance = testExecutionValueModalService.editValue(vm.executionValuesGroup, vm.metric, vm.testExecutionId, index);

            modalInstance.result.then(function () {
                updateMultiValueTable();
            });
        }

        function deleteValueObject(index) {
            var modalInstance =  testExecutionValueModalService.removeValue(vm.executionValuesGroup, vm.testExecutionId, index);

            modalInstance.result.then(function () {
                updateMultiValueTable();
            });
        }

        function updateMultiValueTable() {
            testExecutionService.getById(vm.testExecutionId).then(function(response) {
                vm.executionValuesGroup = getValuesGroupByMetricId(vm.metric.id,
                    response.executionValuesGroups);
            });
        }

        function getValueObjectParameter(parameterName, parameters) {
            return $filter('getByProperty')('name', parameterName, parameters);
        }

        function getValuesGroupByMetricId(metricId, valuesGroups) {
            return $filter('getByProperty')('metricId', metricId, valuesGroups);
        }
    }
})();