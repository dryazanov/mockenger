'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController', ['$scope', 'groupsService', 'currentProject', function ($scope, groupsService, currentProject) {
        $scope.currentProject = currentProject;
        currentProject.$promise.then(function (obj) {

            $scope.groupsList = groupsService.query({projectId: obj.id}, function (response, getResponseHeaders) {
                $scope.groupsList = response;
            }, function (errorResponse) {
            });

        });
    }]);
