'use strict';

angular.module('mockengerClientMainApp')
    .controller('GroupController',['$scope', 'projectListService', 'groupService', 'groupListService', 'API_BASE_PATH',
        function ($scope, projectListService, groupService, groupListService, API_BASE_PATH) {
            var groupModal = $('#groupModal');
            $scope.groupToSave = {};
            $scope.urlToSendRequests = null;

            $scope.$on('openGroupModal', function(event, group) {
                if (group.id == null) {
                    $scope.urlToSendRequests = null;
                    $scope.groupToSave = group;
                } else {
                    $scope.urlToSendRequests = API_BASE_PATH + "/" + projectListService.getCurrent().type + "/" + group.id
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
                        $scope.showGreenMessage('Group <b>' + group.name + '</b> successfully updated');
                    }, function(errorResponse) {
                        $scope.showRedMessage(errorResponse);
                    });
                } else {
                    groupService.ajax.save(group, function(response) {
                        groupModal.modal('hide');
                        groupListService.addGroupToList(response);
                        $scope.showGreenMessage('Group <b>' + group.name + '</b> has been created');
                    }, function(errorResponse) {
                        $scope.showRedMessage(errorResponse);
                    });
                }
            };
}]);