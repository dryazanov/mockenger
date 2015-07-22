'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'projectsService', function ($scope, projectsService) {
        $scope.projectsList = projectsService.query(function(response, getResponseHeaders) {
            $scope.projectsList = response;
        }, function(errorResponse) {
        });
    }]);
