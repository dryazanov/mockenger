'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', '$filter', 'groupsService', 'requestsService', 'currentProject', 'REQUESTS_PER_PAGE', function ($scope, $filter, groupsService, requestsService, currentProject, REQUESTS_PER_PAGE) {

        var REQUEST_VIEW = {
            HEADERS: 'HEADERS',
            PARAMETERS: 'PARAMETERS',
            BODY: 'BODY'
        };

        $scope.currentProject = currentProject;

        $scope.requestsList = {};
        $scope.requestsList.paginator = {};

        //default order criteria
        $scope.requestsList.orderProp = 'name';

        $scope.requestsList.paginator.itemsPerPage = REQUESTS_PER_PAGE;
        $scope.requestsList.paginator.currentPage = 0;

        currentProject.$promise.then(function (obj) {

            groupsService.query({projectId: obj.id}, function (response, getResponseHeaders) {
                $scope.groupsList = response;
            }, function (errorResponse) {
            });

        });

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

        $scope.loadGroupRequests = function (group) {
            requestsService.query({
                projectId: currentProject.id,
                groupId: group.id
            }, function (response, getResponseHeaders) {
                $scope.requestsList.data = response;
                $scope.currentGroup = group;
                $scope.currentRequest = undefined;
                $scope.setHeadersRequestView();
                $scope.requestsList.requestQuery = undefined;
            }, function (errorResponse) {
            });
        };

        $scope.requestsList.editCurrentRequest = function() {
            $('#requestModal').modal({});
        };

        $scope.requestsList.selectRequest = function (request) {
            $scope.currentRequest = request;
        };

        $scope.$watchGroup(['requestsList.orderProp', 'requestsList.requestQuery'], function() {
            $scope.requestsList.paginator.setPage(0);
        });

        $scope.requestsList.paginator.range = function () {
            if ($scope.requestsList.paginator.pageCount() < 0) {
                return [];
            }
            if ($scope.requestsList.paginator.pageCount() == 0) {
                return [0];
            }
            var rangeSize = $scope.requestsList.paginator.itemsPerPage;
            var ret = [];
            var start;

            start = $scope.requestsList.paginator.currentPage;
            if (start > $scope.requestsList.paginator.pageCount() - rangeSize) {
                start = $scope.requestsList.paginator.pageCount() - rangeSize + 1;
            }

            for (var i = start; i < start + rangeSize; i++) {
                ret.push(i);
            }
            return ret;
        };

        $scope.requestsList.paginator.prevPage = function () {
            if ($scope.requestsList.paginator.currentPage > 0) {
                $scope.requestsList.paginator.currentPage--;
            }
        };

        $scope.requestsList.paginator.prevPageDisabled = function () {
            return $scope.requestsList.paginator.currentPage === 0 ? "disabled" : "";
        };

        $scope.requestsList.paginator.pageCount = function () {
            var filtered = $filter('filter')($scope.requestsList.data, $scope.requestsList.requestQuery);
            return Math.ceil(filtered.length / $scope.requestsList.paginator.itemsPerPage) - 1;
        };

        $scope.requestsList.paginator.nextPage = function () {
            if ($scope.requestsList.paginator.currentPage < $scope.requestsList.paginator.pageCount()) {
                $scope.requestsList.paginator.currentPage++;
            }
        };

        $scope.requestsList.paginator.setPage = function (n) {
            $scope.requestsList.paginator.currentPage = n;
        };

        $scope.requestsList.paginator.nextPageDisabled = function () {
            return $scope.requestsList.paginator.currentPage === $scope.requestsList.paginator.pageCount() ? "disabled" : "";
        };


    }]);
