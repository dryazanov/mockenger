'use strict';

angular.module('mockengerClientMainApp').controller('GroupListController', ['$scope', 'projectListService', 'groupService', 'groupListService', 'requestListService',
    function($scope, projectListService, groupService, groupListService, requestListService) {

    requestListService.setCurrent(null);
    requestListService.setData(null);

    $scope.isActive = function(group) {
        return (group === groupListService.getCurrent() ? true : false);
    }

    $scope.loadGroupRequests = function(group) {
        if (groupListService.getCurrent() == null || group.id !== groupListService.getCurrent().id) {
            requestListService.setCurrent(null);
            groupListService.setCurrent(group);
            requestListService.ajax.query({projectId: projectListService.getCurrent().id, groupId: groupListService.getCurrent().id}, function(response, getResponseHeaders) {
                requestListService.setData(response);
            }, function (errorResponse) {

            });
        }
    }

    $scope.createGroup = function() {
        groupService.openGroupModal(projectListService.getCurrent(), null);
    }

    $scope.editGroup = function(group, index) {
        groupService.openGroupModal(projectListService.getCurrent(), group);
    }

    $scope.deleteGroup = function(index, group) {
        if (groupListService.getData() != null && groupListService.getData()[index] != null) {
            groupService.ajax.delete({projectId: projectListService.getCurrent(), groupId: group.id}, function(response, getResponseHeaders) {
                if (group == groupListService.getCurrent()) {
                    groupListService.setCurrent(null);
                    requestListService.setData(null);
                    requestListService.setCurrent(null);
                }
                groupListService.removeFromGroupList(index);
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        }
    };
}]);