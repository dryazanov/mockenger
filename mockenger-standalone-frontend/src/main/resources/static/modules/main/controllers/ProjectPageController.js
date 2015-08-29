'use strict';

angular.module('mockengerClientMainApp').controller('ProjectPageController', ['$scope', 'currentProject', 'projectsService', 'groupsService', 'requestsService',
        function($scope, currentProject, projectsService, groupsService, requestsService) {

    $scope.currentProject = currentProject;
    $scope.groupsService = groupsService;
    $scope.requestsService = requestsService;

    groupsService.setCurrent(null);
    groupsService.setData(null);

    currentProject.$promise.then(function (obj) {
        projectsService.setCurrent(obj);
        groupsService.ajax.query({projectId: currentProject.id}, function(response, getResponseHeaders) {
            groupsService.setData(response);
        }, function (errorResponse) {

        });
    });
}]);