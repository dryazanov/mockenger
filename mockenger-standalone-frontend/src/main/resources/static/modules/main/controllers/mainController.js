'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'projectsService', function ($scope, projectsService) {
        $scope.projectsList = projectsService.query(function(response, getResponseHeaders) {
            console.info('here');
            console.info(response);
            $scope.projectsList = response;
        }, function(errorResponse) {
            console.info(errorResponse);
        });
    }]);
