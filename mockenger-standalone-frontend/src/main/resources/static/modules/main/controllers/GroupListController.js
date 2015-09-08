'use strict';

angular.module('mockengerClientMainApp').controller('GroupListController', ['$scope', 'projectListService', 'groupService', 'groupListService', 'requestListService', 'API_BASE_PATH',
    function($scope, projectListService, groupService, groupListService, requestListService, API_BASE_PATH) {
        requestListService.setCurrent(null);
        requestListService.setData(null);

        $scope.isActive = function(group) {
            return (group === groupListService.getCurrent() ? true : false);
        }

        $scope.loadGroupRequests = function(group) {
            if (groupListService.getCurrent() == null || group.id !== groupListService.getCurrent().id) {
                requestListService.setCurrent(null);
                groupListService.setCurrent(group);
                groupListService.setUrlForNewRequests(API_BASE_PATH + "/" + projectListService.getCurrent().type + "/" + group.id);

                var paramsToSend = {
                    projectId: projectListService.getCurrent().id,
                    groupId: groupListService.getCurrent().id
                };
                requestListService.ajax.query(paramsToSend, function(response, getResponseHeaders) {
                    requestListService.setData(response);
                }, function (errorResponse) {
                    $scope.showRedMessage(errorResponse);
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
                    $scope.showGreenMessage('Group <b>' + group.name + '</b> deleted');
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }
        };
}]);