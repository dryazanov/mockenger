'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController',['$scope', 'projectsService', function ($scope, projectsService) {
        $scope.test = 'TEST';
    }]);
