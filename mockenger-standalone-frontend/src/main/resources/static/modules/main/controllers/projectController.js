'use strict';

module.controller('projectController', ['$scope', '$filter', 'currentProject', 'groupsService', 'requestsService', function($scope, $filter, currentProject, groupsService, requestsService) {
    $scope.currentProject = currentProject;

    currentProject.$promise.then(function (obj) {
        groupsService.query({projectId: currentProject.id}, function (response, getResponseHeaders) {
            $scope.groupsList = response;
        }, function (errorResponse) {

        });
    });
}]);


module.controller('groupController', ['$rootScope', '$scope', '$filter', 'projectsService', 'groupsService', 'requestsService', function($rootScope, $scope, $filter, projectsService, groupsService, requestsService) {
    $scope.loadGroupRequests = function(group) {
        requestsService.ajax.query({
            projectId: projectsService.projectId,
            groupId: group.id
        }, function (response, getResponseHeaders) {
            requestsService.refreshData(response);
            $rootScope.currentGroup = group;
        }, function (errorResponse) {

        });
    }
}]);


module.controller('requestController', ['$scope', '$filter', 'requestsService', 'REQUESTS_PER_PAGE', function($scope, $filter, requestsService, REQUESTS_PER_PAGE) {
    $scope.service = requestsService;

    var REQUEST_VIEW = {
        HEADERS: 'HEADERS',
        PARAMETERS: 'PARAMETERS',
        BODY: 'BODY'
    };

    $scope.setHeadersRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.HEADERS;
    };

    $scope.setParamsRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.PARAMETERS;
    };

    $scope.setBodyRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.BODY;
    };

    $scope.isHeadersRequestViewActive = function() {
      return $scope.currentRequestView === REQUEST_VIEW.HEADERS;
    };

    $scope.isBodyRequestViewActive = function() {
        return $scope.currentRequestView === REQUEST_VIEW.BODY;
    };

    $scope.isParamsRequestViewActive = function() {
        return $scope.currentRequestView === REQUEST_VIEW.PARAMETERS;
    };

//        $scope.requestsList.editCurrentRequest = function() {
//            $('#requestModal').modal({});
//        };

    $scope.getRequestList = function() {
        return requestsService.getData();
    }

    $scope.selectRequest = function(request) {
//        $scope.currentRequest = request;
        requestsService.selectedRequest = request;
    };

    $scope.getSelectRequest = function() {
        return requestsService.selectedRequest;
    };

    $scope.$watchGroup(['service.requestsList.orderProp', 'service.requestsList.requestQuery'], function() {
        $scope.setPage(0);
    });

    $scope.range = function() {
        if ($scope.pageCount() < 0) {
            return [];
        }
        if ($scope.pageCount() == 0) {
            return [0];
        }
        var rangeSize = $scope.getItemsPerPage();
        var ret = [];
        var start;

        start = $scope.getCurrentPage();
        if (start > $scope.pageCount() - rangeSize) {
            start = $scope.pageCount() - rangeSize + 1;
        }

        for (var i = start; i < start + rangeSize; i++) {
            ret.push(i);
        }
        return ret;
    };

    $scope.prevPage = function() {
        if ($scope.getCurrentPage() > 0) {
            $scope.setPage($scope.getCurrentPage() - 1);
        }
    };

    $scope.prevPageDisabled = function() {
        return $scope.getCurrentPage() === 0 ? "disabled" : "";
    };

    $scope.pageCount = function() {
        if (requestsService.getData() != null) {
            var filtered = $filter('filter')(requestsService.getData(), requestsService.requestsList.requestQuery);
            return Math.ceil(filtered.length / $scope.getItemsPerPage()) - 1;
        }
        return 0;
    };

    $scope.nextPage = function() {
        if ($scope.getCurrentPage() < $scope.pageCount()) {
            $scope.setPage($scope.getCurrentPage() + 1);
        }
    };

    $scope.getCurrentPage = function() {
        return requestsService.getCurrentPage();
    };

    $scope.setPage = function(n) {
        requestsService.setCurrentPage(n);
    };

    $scope.nextPageDisabled = function () {
        return $scope.getCurrentPage() === $scope.pageCount() ? "disabled" : "";
    };

    $scope.getItemsPerPage = function() {
        return REQUESTS_PER_PAGE;
    }

    $scope.getRequestQuery = function() {
        return requestsService.requestsList.requestQuery;
    }

    $scope.getOrderProp = function() {
        return requestsService.requestsList.orderProp;
    }
}]);
