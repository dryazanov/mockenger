'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', 'groupsService', 'requestsService', 'currentProject', function ($scope, groupsService, requestsService, currentProject) {
        $scope.data = {};

        $scope.data.currentProject = currentProject;

        //default order criteria
        $scope.data.orderProp = 'name';

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
            }, function (errorResponse) {
            });
        };

        $scope.selectRequest = function(request) {
            $scope.data.currentRequest = request;
        };

    }]);
