'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', 'groupsService', 'requestsService', 'currentProject', function ($scope, groupsService, requestsService, currentProject) {
        $scope.currentProject = currentProject;
        currentProject.$promise.then(function (obj) {

            groupsService.query({projectId: obj.id}, function (response, getResponseHeaders) {
                $scope.groupsList = response;
            }, function (errorResponse) {
            });

        });

        $scope.loadGroupRequests = function (group) {
            requestsService.query({
                projectId: currentProject.id,
                groupId: group.id
            }, function (response, getResponseHeaders) {
                $scope.requestsList = response;
                $scope.currentGroup = group;
                $scope.currentRequest = undefined;
            }, function (errorResponse) {
            });
        }

        $scope.selectRequest = function(request) {
            $scope.currentRequest = request;
        }

    }]);
