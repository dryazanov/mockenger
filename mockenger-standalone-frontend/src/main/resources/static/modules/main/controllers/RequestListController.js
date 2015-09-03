'use strict';

angular.module('mockengerClientMainApp').controller('RequestListController', ['$scope', '$filter', 'requestListService', 'REQUESTS_PER_PAGE',
    function($scope, $filter, requestListService, REQUESTS_PER_PAGE) {

    $scope.searchQuery = requestListService.getSearchQuery();
    $scope.listOrder = requestListService.getListOrder();
    requestListService.setLimit(REQUESTS_PER_PAGE);

    $scope.$watchGroup([requestListService.getListOrder, requestListService.getSearchQuery], function() {
        requestListService.setCurrentPage(0)
    });

    $scope.range = function() {
        if ($scope.pageCount() < 0) {
            return [];
        }
        if ($scope.pageCount() == 0) {
            return [0];
        }
        var rangeSize = requestListService.getLimit();
        var ret = [];
        var start;

        start = requestListService.getCurrentPage();
        if (start > $scope.pageCount() - rangeSize) {
            start = $scope.pageCount() - rangeSize + 1;
        }

        for (var i = start; i < start + rangeSize; i++) {
            ret.push(i);
        }
        return ret;
    };

    $scope.prevPage = function() {
        if (requestListService.getCurrentPage() > 0) {
            requestListService.setCurrentPage(requestListService.getCurrentPage() - 1);
        }
    };

    $scope.isActive = function(n) {
        return (requestListService.getCurrentPage() == n ? "active" : "");
    };

    $scope.nextPage = function() {
        if (requestListService.getCurrentPage() < $scope.pageCount()) {
            requestListService.setCurrentPage(requestListService.getCurrentPage() + 1);
        }
    };

    $scope.prevPageDisabled = function() {
        return (requestListService.getCurrentPage() === 0 ? "disabled" : "");
    };

    $scope.pageCount = function() {
        if (requestListService.getData() != null) {
            var filtered = $filter('filter')(requestListService.getData(), requestListService.getSearchQuery());
            return Math.ceil(filtered.length / requestListService.getLimit()) - 1;
        }
        return 0;
    };

    $scope.nextPageDisabled = function () {
        return requestListService.getCurrentPage() === $scope.pageCount() ? "disabled" : "";
    };

    $scope.getOffset = function() {
        return (requestListService.getCurrentPage() * requestListService.getLimit());
    }
}]);