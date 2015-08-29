'use strict';

angular.module('mockengerClientMainApp').controller('GroupListController', ['$scope', 'projectsService', 'groupService', 'groupsService', 'requestsService',
    function($scope, projectsService, groupService, groupsService, requestsService) {

    requestsService.setCurrent(null);
    requestsService.setData(null);

    $scope.isActive = function(group) {
        return (group === groupsService.getCurrent() ? "active" : "");
    }

    $scope.loadGroupRequests = function(group) {
        if (groupsService.getCurrent() == null || group.id !== groupsService.getCurrent().id) {
            requestsService.setCurrent(null);
            groupsService.setCurrent(group);
            requestsService.ajax.query({projectId: projectsService.getCurrent().id, groupId: groupsService.getCurrent().id}, function(response, getResponseHeaders) {
                requestsService.setData(response);
            }, function (errorResponse) {

            });
        }
    }

    $scope.createGroup = function() {
        groupService.openGroupModal(projectsService.getCurrent(), null);
    }

    $scope.editGroup = function(group, index) {
        groupService.openGroupModal(projectsService.getCurrent(), group);
    }

    $scope.deleteGroup = function(index, group) {
        groupService.ajax.delete({projectId: projectsService.getCurrent(), groupId: group.id}, function(response, getResponseHeaders) {
            if (group == groupsService.getCurrent()) {
                groupsService.setCurrent(null);
                requestsService.setData(null);
                requestsService.setCurrent(null);
            }
            groupsService.getData().splice(index, 1);
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    };
}]);