'use strict';

angular.module('mockengerClientMainApp')
    .controller('projectController',['$scope', 'projectsService', 'currentProject', function ($scope, projectsService, currentProject) {
        $scope.currentProject = currentProject;
    }]);
