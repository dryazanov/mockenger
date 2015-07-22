'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'projectsService', function ($scope, projectsService) {
        projectsService.query(function(response, getResponseHeaders) {
            $scope.projectsList = response;
        }, function(errorResponse) {
        });
    }]);
