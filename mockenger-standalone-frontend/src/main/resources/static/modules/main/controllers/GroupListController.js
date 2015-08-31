'use strict';

angular.module('mockengerClientMainApp').controller('GroupListController', ['$scope', 'projectsService', 'groupService', 'groupListService', 'requestsService',
    function($scope, projectsService, groupService, groupListService, requestsService) {

    requestsService.setCurrent(null);
    requestsService.setData(null);

    $scope.isActive = function(group) {
        return (group === groupListService.getCurrent() ? true : false);
    }

    $scope.loadGroupRequests = function(group) {
        if (groupListService.getCurrent() == null || group.id !== groupListService.getCurrent().id) {
            requestsService.setCurrent(null);
            groupListService.setCurrent(group);
            requestsService.ajax.query({projectId: projectsService.getCurrent().id, groupId: groupListService.getCurrent().id}, function(response, getResponseHeaders) {
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
        if (groupListService.getData() != null && groupListService.getData()[index] != null) {
            groupService.ajax.delete({projectId: projectsService.getCurrent(), groupId: group.id}, function(response, getResponseHeaders) {
                if (group == groupListService.getCurrent()) {
                    groupListService.setCurrent(null);
                    requestsService.setData(null);
                    requestsService.setCurrent(null);
                }
                groupListService.removeFromGroupList(index);
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        }
    };
}]);