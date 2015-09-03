'use strict';

angular.module('mockengerClientMainApp').controller('ProjectPageController', ['$scope', 'currentProject', 'projectsService', 'groupListService', 'requestsService', 'valuesetService',
        function($scope, currentProject, projectsService, groupListService, requestsService, valuesetService) {

    $scope.currentProject = currentProject;
    $scope.groupListService = groupListService;
    $scope.requestsService = requestsService;

    // ValueSets
    $scope.requestMethods = [];
    $scope.transformerTypes = [];

    // Reset current group
    groupListService.setCurrent(null);
    groupListService.setData(null);

    currentProject.$promise.then(function (obj) {
        projectsService.setCurrent(obj);
        $scope.getRequestMethods(obj.id);
        $scope.getTransformerTypes();

        groupListService.ajax.query({projectId: currentProject.id}, function(response, getResponseHeaders) {
            groupListService.setData(response);
        }, function (errorResponse) {

        });
    });

    $scope.getRequestMethods = function(projectId) {
        valuesetService.requestMethods.get({projectId: projectId}, function(response, getResponseHeaders) {
            $scope.requestMethods = response;
        }, function(errorResponse) {
            //showErrors(errorResponse);
        });
    }

    $scope.getTransformerTypes = function() {
        valuesetService.transformerTypes.get(function(response, getResponseHeaders) {
            $scope.transformerTypes = response;
        }, function(errorResponse) {
            //showErrors(errorResponse);
        });
    }
}]);