'use strict';

angular.module('mockengerClientMainApp')
	.controller('GroupListController', [
		'$scope',
		'$confirm',
		'projectListService',
		'groupService',
		'groupListService',
		'requestListService',
		'API_BASE_PATH',

		function($scope, $confirm, projectListService, groupService, groupListService, requestListService, API_BASE_PATH) {
			requestListService.setCurrent(null);
			requestListService.setData(null);

			$scope.isActive = function(group) {
				return (group === groupListService.getCurrent() ? true : false);
			}

			$scope.loadGroupRequests = function(group) {
				requestListService.setCurrent(null);
				groupListService.setCurrent(group);
				groupListService.setUrlForNewRequests(API_BASE_PATH + "/" + projectListService.getCurrent().type + "/" + group.code + "/");

				var paramsToSend = {
					projectCode: projectListService.getCurrent().code,
					groupCode: groupListService.getCurrent().code
				};

				requestListService.ajax.query(paramsToSend, function(response, getResponseHeaders) {
					requestListService.setData(response);
				}, function (errorResponse) {
					$scope.showRedMessage(errorResponse);
				});
			}

			$scope.createGroup = function() {
				groupService.openGroupModal(projectListService.getCurrent(), null);
			}

			$scope.editGroup = function(group, index) {
				groupService.openGroupModal(projectListService.getCurrent(), group);
			}

			$scope.deleteGroup = function(index, group) {
				$confirm({
					text: "Do you really want to delete group '" + group.name + "' with all its mocks?"
				}).then(function() {
					if (groupListService.getData() != null && groupListService.getData()[index] != null) {
						groupService.ajax.delete({projectCode: projectListService.getCurrent().code, groupCode: group.code}, function(response, getResponseHeaders) {
							if (group == groupListService.getCurrent()) {
								groupListService.setCurrent(null);
								requestListService.setData(null);
								requestListService.setCurrent(null);
							}
							groupListService.removeFromGroupList(index);
							$scope.showGreenMessage('Group <b>' + group.name + '</b> deleted');
							confirmService.hideDialog();
						}, function(errorResponse) {
							$scope.showRedMessage(errorResponse);
							confirmService.hideDialog();
						});
					}
				}, function() {
					// cancel
				});
			}
}]);