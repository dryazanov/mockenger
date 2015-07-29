'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', '$filter', 'groupsService', 'requestsService', 'currentProject', function ($scope, $filter, groupsService, requestsService, currentProject) {
        $scope.data = {};

        $scope.data.currentProject = currentProject;

        $scope.data.requestsList = {};
        $scope.data.requestsList.paginator = {};

        //default order criteria
        $scope.data.requestsList.orderProp = 'name';

        $scope.data.requestsList.paginator.itemsPerPage = 2;
        $scope.data.requestsList.paginator.currentPage = 0;

        currentProject.$promise.then(function (obj) {

            groupsService.query({projectId: obj.id}, function (response, getResponseHeaders) {
                $scope.data.groupsList = response;
            }, function (errorResponse) {
            });

        });

        $scope.loadGroupRequests = function (group) {
            requestsService.query({
                projectId: currentProject.id,
                groupId: group.id
            }, function (response, getResponseHeaders) {
                $scope.data.requestsList.data = response;
                $scope.data.currentGroup = group;
                $scope.data.currentRequest = undefined;
                $scope.data.requestsList.requestQuery = undefined;
            }, function (errorResponse) {
            });
        };

        $scope.selectRequest = function (request) {
            $scope.data.currentRequest = request;
        };

        $scope.data.requestsList.paginator.range = function () {
            if ($scope.data.requestsList.paginator.pageCount() < 0) {
                return [];
            }
            if ($scope.data.requestsList.paginator.pageCount() == 0) {
                return [0];
            }
            var rangeSize = $scope.data.requestsList.paginator.itemsPerPage;
            var ret = [];
            var start;

            start = $scope.data.requestsList.paginator.currentPage;
            if (start > $scope.data.requestsList.paginator.pageCount() - rangeSize) {
                start = $scope.data.requestsList.paginator.pageCount() - rangeSize + 1;
            }

            for (var i = start; i < start + rangeSize; i++) {
                ret.push(i);
            }
            return ret;
        };

        $scope.data.requestsList.paginator.prevPage = function () {
            if ($scope.data.requestsList.paginator.currentPage > 0) {
                $scope.data.requestsList.paginator.currentPage--;
            }
        };

        $scope.data.requestsList.paginator.prevPageDisabled = function () {
            return $scope.data.requestsList.paginator.currentPage === 0 ? "disabled" : "";
        };

        $scope.data.requestsList.paginator.pageCount = function () {
            var filtered = $filter('filter')($scope.data.requestsList.data, $scope.data.requestsList.requestQuery);
            return Math.ceil(filtered.length / $scope.data.requestsList.paginator.itemsPerPage) - 1;
        };

        $scope.data.requestsList.paginator.nextPage = function () {
            if ($scope.data.requestsList.paginator.currentPage < $scope.data.requestsList.paginator.pageCount()) {
                $scope.data.requestsList.paginator.currentPage++;
            }
        };

        $scope.data.requestsList.paginator.setPage = function (n) {
            $scope.data.requestsList.paginator.currentPage = n;
        };

        $scope.data.requestsList.paginator.nextPageDisabled = function () {
            return $scope.data.requestsList.paginator.currentPage === $scope.data.requestsList.paginator.pageCount() ? "disabled" : "";
        };


    }]);
