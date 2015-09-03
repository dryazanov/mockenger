'use strict';

angular.module('mockengerClientMainApp').controller('GroupController',['$scope', 'projectListService', 'groupService', 'groupListService',
    function ($scope, projectListService, groupService, groupListService) {

        var groupModal = $('#groupModal');
        $scope.groupToSave = {};

        $scope.$on('openGroupModal', function(event, group) {
            if (group.id == null) {
                $scope.groupToSave = group;
            } else {
                $scope.groupToSave = {};
                angular.copy(group, $scope.groupToSave);
            }
            $scope.groupForm.$setPristine();
            groupModal.modal({});
        });

        $scope.saveGroup = function(group) {
            if (group.id != null) {
                groupService.ajax.update({groupId: group.id}, group, function(response, getResponseHeaders) {
                    groupModal.modal('hide');
                    for (var i = 0, l = groupListService.getData().length; i < l; i++) {
                        if (groupListService.getData()[i].id == response.id) {
                            groupListService.getData()[i] = response;
                            break;
                        }
                    }
                }, function(errorResponse) {
                    showErrors(errorResponse);
                });
            } else {
                groupService.ajax.save(group, function(response) {
                    groupModal.modal('hide');
                    groupListService.addGroupToList(response);
                }, function(errorResponse) {
                    showErrors(errorResponse);
                });
            }
        };
}]);