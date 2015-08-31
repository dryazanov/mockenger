'use strict';

angular.module('mockengerClientMainApp').controller('ProjectPageController', ['$scope', 'currentProject', 'projectsService', 'groupListService', 'requestsService',
        function($scope, currentProject, projectsService, groupListService, requestsService) {

    $scope.currentProject = currentProject;
    $scope.groupListService = groupListService;
    $scope.requestsService = requestsService;

    groupListService.setCurrent(null);
    groupListService.setData(null);

    currentProject.$promise.then(function (obj) {
        projectsService.setCurrent(obj);
        groupListService.ajax.query({projectId: currentProject.id}, function(response, getResponseHeaders) {
            groupListService.setData(response);
        }, function (errorResponse) {

        });
    });
}]);