'use strict';

angular.module('mockengerClientMainApp').controller('ProjectPageController', ['$scope', 'currentProject', 'projectListService', 'groupListService', 'requestListService', 'valuesetService',
        function($scope, currentProject, projectListService, groupListService, requestListService, valuesetService) {

    $scope.currentProject = currentProject;
    $scope.groupListService = groupListService;
    $scope.requestListService = requestListService;

    // ValueSets
    $scope.requestMethods = [];
    $scope.transformerTypes = [];

    // Reset current group
    groupListService.setCurrent(null);
    groupListService.setData(null);

    currentProject.$promise.then(function (obj) {
        projectListService.setCurrent(obj);
        $scope.getRequestMethods(obj.id);
        $scope.getTransformerTypes();

        groupListService.ajax.query({projectId: currentProject.id}, function(response, getResponseHeaders) {
            groupListService.setData(response);
        }, function (errorResponse) {
            $scope.showRedMessage(errorResponse);
        });
    });

    $scope.getRequestMethods = function(projectId) {
        valuesetService.requestMethods.get({projectId: projectId}, function(response, getResponseHeaders) {
            $scope.requestMethods = response;
        }, function(errorResponse) {
            $scope.showRedMessage(errorResponse);
        });
    }

    $scope.getTransformerTypes = function() {
        valuesetService.transformerTypes.get(function(response, getResponseHeaders) {
            $scope.transformerTypes = response;
        }, function(errorResponse) {
            $scope.showRedMessage(errorResponse);
        });
    }
}]);