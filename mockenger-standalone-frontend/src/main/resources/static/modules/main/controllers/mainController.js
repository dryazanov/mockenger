'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'projectsService', function ($scope, projectsService) {
        $scope.data = {};
        projectsService.query(function(response, getResponseHeaders) {
            $scope.data.projectsList = response;
        }, function(errorResponse) {
        });
    }]);
