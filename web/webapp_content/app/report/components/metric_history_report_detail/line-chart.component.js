(function() {
    'use strict';

    angular
        .module('org.perfrepo.report.detail')
        .component('metricHistoryLineChart', {
            bindings: {
                chartData: '<'
            },
            controller: MetricHistoryLineChartController,
            controllerAs: 'vm',
            templateUrl: 'app/report/components/metric_history_report_detail/line-chart.view.html'
        });

    function MetricHistoryLineChartController(reportModalService, CHART_COLORS) {
        var vm = this;
        var colorIndex = 0;
        vm.data = [];
        // series
        angular.forEach(vm.chartData.series, function(series) {
            vm.data.push({
                key: series.name,
                values: series.values,
                color: CHART_COLORS[colorIndex++ % CHART_COLORS.length]
            });
        });
        // baselines
        angular.forEach(vm.chartData.baselines, function(baseline) {
            var data = [
                    {
                        x: baseline.value.x1,
                        y: baseline.value.y,
                        executionId: baseline.value.executionId,
                        executionName: baseline.value.executionName
                    },
                    {
                        x: baseline.value.x2,
                        y: baseline.value.y,
                        executionId: baseline.value.executionId,
                        executionName: baseline.value.executionName
                    }
                ];

            vm.data.push({
                key: baseline.name,
                values: data,
                color: CHART_COLORS[colorIndex++ % CHART_COLORS.length]
            });
        });

        vm.options = {
            chart: {
                type: 'lineChart',
                height: 450,
                margin : {
                    top: 40,
                    right: 20,
                    bottom: 40,
                    left: 95
                },
                x: function(d){ return d.x; },
                y: function(d){ return d.y; },
                valueFormat: function(d){
                    return d3.format(',.4f')(d);
                },
                useInteractiveGuideline: false,
                xAxis: {
                    axisLabel: "Test execution"
                },
                yAxis: {
                    axisLabel: "Metric value",
                    tickFormat: function(d){
                        return d3.format('.0f')(d);
                    },
                    axisLabelDistance: 18
                },
                lines: {
                    dispatch: {
                        elementClick: function(e) {
                            pointClicked(e);
                        }
                    }
                }
            }
        };

        function pointClicked(e) {
            reportModalService.showLineChartPointDetail(e.point, e.series);
        }
    }
})();

