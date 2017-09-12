'use strict';

angular.module('mockengerClientMainApp')
	.controller('AdminPageController',['$scope', 'accountService', 'accountListService', 'eventListService', 'valuesetService', 'API_BASE_PATH',
		function ($scope, accountService, accountListService, eventListService, valuesetService, API_BASE_PATH) {

			$scope.roles = {};
			$scope.eventEntityType = "ACCOUNT";

			$scope.createAccount = function() {
				accountService.openAccountModal(null);
			};

			$scope.editAccount = function(account) {
				accountService.openAccountModal(account);
			};

			$scope.getRoles = function() {
				valuesetService.roles.get(function(response, getResponseHeaders) {
					$scope.roles = response;
				}, function(errorResponse) {
					$scope.showRedMessage(errorResponse);
				});
			}

			$scope.loadAccountEvents = function() {
				var page = 0;
				eventListService.events.get({types: $scope.eventEntityType, page: page}, function(response) {
					eventListService.setCurrentPage(page);
					eventListService.setEntityType($scope.eventEntityType);
					eventListService.setData(response);
				}, function (errorResponse) {
					$scope.showRedMessage(errorResponse);
				});
			}

			$scope.getRoles();
			$scope.loadAccountEvents();
}]);