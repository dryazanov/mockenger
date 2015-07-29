'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', '$filter', 'groupsService', 'requestsService', 'currentProject', function ($scope, $filter, groupsService, requestsService, currentProject) {
        $scope.data = {};

        $scope.data.currentProject = currentProject;

        //default order criteria
        $scope.data.orderProp = 'name';

        $scope.data.itemsPerPage = 2;
        $scope.data.currentPage = 0;

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
                $scope.data.requestsList = response;
                $scope.data.currentGroup = group;
                $scope.data.currentRequest = undefined;
                $scope.data.requestQuery = undefined;
            }, function (errorResponse) {
            });
        };

        $scope.selectRequest = function (request) {
            $scope.data.currentRequest = request;
        };

        $scope.range = function () {
            if ($scope.pageCount() < 0) {
                return [];
            }
            if ($scope.pageCount() == 0) {
                return [0];
            }
            var rangeSize = 2;
            var ret = [];
            var start;

            start = $scope.data.currentPage;
            if (start > $scope.pageCount() - rangeSize) {
                start = $scope.pageCount() - rangeSize + 1;
            }

            for (var i = start; i < start + rangeSize; i++) {
                ret.push(i);
            }
            return ret;
        };

        $scope.prevPage = function () {
            if ($scope.data.currentPage > 0) {
                $scope.data.currentPage--;
            }
        };

        $scope.prevPageDisabled = function () {
            return $scope.data.currentPage === 0 ? "disabled" : "";
        };

        $scope.pageCount = function () {
            var filtered = $filter('filter')($scope.data.requestsList, $scope.data.requestQuery);
            return Math.ceil(filtered.length / $scope.data.itemsPerPage) - 1;
        };

        $scope.nextPage = function () {
            if ($scope.data.currentPage < $scope.pageCount()) {
                $scope.data.currentPage++;
            }
        };

        $scope.setPage = function (n) {
            $scope.data.currentPage = n;
        };

        $scope.nextPageDisabled = function () {
            return $scope.data.currentPage === $scope.pageCount() ? "disabled" : "";
        };


    }]);
