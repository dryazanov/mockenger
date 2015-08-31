'use strict';

angular.module('mockengerClientMainApp').controller('RequestListController', ['$scope', '$filter', 'requestsService', 'REQUESTS_PER_PAGE',
    function($scope, $filter, requestsService, REQUESTS_PER_PAGE) {

    $scope.searchQuery = requestsService.getSearchQuery();
    $scope.listOrder = requestsService.getListOrder();
    requestsService.setLimit(REQUESTS_PER_PAGE);

    $scope.$watchGroup([requestsService.getListOrder, requestsService.getSearchQuery], function() {
        requestsService.setCurrentPage(0)
    });

    $scope.range = function() {
        if ($scope.pageCount() < 0) {
            return [];
        }
        if ($scope.pageCount() == 0) {
            return [0];
        }
        var rangeSize = requestsService.getLimit();
        var ret = [];
        var start;

        start = requestsService.getCurrentPage();
        if (start > $scope.pageCount() - rangeSize) {
            start = $scope.pageCount() - rangeSize + 1;
        }

        for (var i = start; i < start + rangeSize; i++) {
            ret.push(i);
        }
        return ret;
    };

    $scope.prevPage = function() {
        if (requestsService.getCurrentPage() > 0) {
            requestsService.setCurrentPage(requestsService.getCurrentPage() - 1);
        }
    };

    $scope.isActive = function(n) {
        return (requestsService.getCurrentPage() == n ? "active" : "");
    };

    $scope.nextPage = function() {
        if (requestsService.getCurrentPage() < $scope.pageCount()) {
            requestsService.setCurrentPage(requestsService.getCurrentPage() + 1);
        }
    };

    $scope.prevPageDisabled = function() {
        return (requestsService.getCurrentPage() === 0 ? "disabled" : "");
    };

    $scope.pageCount = function() {
        if (requestsService.getData() != null) {
            var filtered = $filter('filter')(requestsService.getData(), requestsService.getSearchQuery());
            return Math.ceil(filtered.length / requestsService.getLimit()) - 1;
        }
        return 0;
    };

    $scope.nextPageDisabled = function () {
        return requestsService.getCurrentPage() === $scope.pageCount() ? "disabled" : "";
    };

    $scope.getOffset = function() {
        return (requestsService.getCurrentPage() * requestsService.getLimit());
    }
}]);