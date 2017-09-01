'use strict';

angular.module('mockengerClientMainApp')
	.controller('GroupController',['$scope', '$filter', 'projectListService', 'groupService', 'groupListService', 'API_BASE_PATH',
		function ($scope, $filter, projectListService, groupService, groupListService, API_BASE_PATH) {
			var groupModal = $('#groupModal');
			$scope.groupToSave = {};
			$scope.urlToSendRequests = null;

			// Change value to uppercase in the field 'groupToSave.code'
			$scope.$watch('groupToSave.code', function(value) {
				if (value != null) {
					$scope.groupToSave.code = $filter('uppercase')(value);
				}
			});

			$scope.$on('openGroupModal', function(event, group) {
				if (group.id == null) {
					$scope.urlToSendRequests = null;
					$scope.groupToSave = group;
				} else {
					$scope.urlToSendRequests = API_BASE_PATH + "/" + projectListService.getCurrent().type + "/" + group.code + "/"
					$scope.groupToSave = {};
					angular.copy(group, $scope.groupToSave);
				}
				$scope.groupForm.$setPristine();
				groupModal.modal({});
			});

			$scope.saveGroup = function(group) {
				var requestParams = {
					projectCode: projectListService.getCurrent().code,
				}

				if (group.id != null) {
					requestParams.groupCode = group.code;

					groupService.ajax.update(requestParams, group, function(response, getResponseHeaders) {
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

			$scope.generateGroupCode = function() {
				if ($scope.groupToSave.name.length > 0) {
					$scope.groupToSave.code = $scope.groupToSave.name.replace(/[aeiou\s]/ig, '');
				}
			}
}]);