'use strict';

angular.module('mockengerClientMainApp')
	.controller('IndexPageController', ['$rootScope', '$scope', '$filter', '$confirm', 'projectListService', 'valuesetService',
		function ($rootScope, $scope, $filter, $confirm, projectListService, valuesetService) {
			var projectModal = $('#projectModal');
			$scope.projectListService = projectListService;
			$scope.projectsList = {};
			$scope.availableProjectTypes = {};

			// Change value to uppercase in the field 'Code'
			$scope.$watch('currentProject.code', function(value) {
				if (value != null) {
					$scope.currentProject.code = $filter('uppercase')(value);
				}
			});

			$scope.getProjectTypes = function() {
				valuesetService.projectTypes.get(function(response, getResponseHeaders) {
					$scope.availableProjectTypes = response;
				}, function(errorResponse) {
					$scope.showRedMessage(errorResponse);
				});
			}

			$scope.getProjects = function() {
				projectListService.ajax.query(function(response, getResponseHeaders) {
					projectListService.setData(response);
				}, function(errorResponse) {
					$scope.showRedMessage(errorResponse);
				});
			}

			$scope.createProject = function() {
				$scope.alerts = [];
				$scope.currentProject = {};
				$scope.projectForm.$setPristine();
				projectModal.modal({});
			};

			$scope.editProject = function(project) {
				$scope.alerts = [];
				$scope.currentProject = {};
				angular.copy(project, $scope.currentProject);
				projectModal.modal({});
			};

			$scope.deleteProject = function(project) {
				$confirm({
					text: "Do you really want to delete project '" + project.name + "' with all its groups and mocks?"
				}).then(function() {
					projectListService.ajax.delete({projectCode: project.code}, function(response, getResponseHeaders) {
						$scope.getProjects();
						$scope.showGreenMessage('Project <b>' + project.name + '</b> deleted');
					}, function(errorResponse) {
						$scope.showRedMessage(errorResponse);
					});
				}, function() {
					// cancel
				});
			};

			$scope.saveProject = function(project) {
				if (project.id != null) {
					projectListService.ajax.update({projectCode: project.code}, project, function(response, getResponseHeaders) {
						projectModal.modal('hide');
						$scope.getProjects();
						$scope.showGreenMessage('Project <b>' + project.name + '</b> successfully updated');
					}, function(errorResponse) {
						$scope.showRedMessage(errorResponse);
					});
				} else {
					projectListService.ajax.save(project, function() {
						projectModal.modal('hide');
						$scope.getProjects();
						$scope.showGreenMessage('Project <b>' + project.name + '</b> has been created');
					}, function(errorResponse) {
						$scope.showRedMessage(errorResponse);
					});
				}
			};

			projectListService.ajax.query(function(response) {
				projectListService.setData(response);
				$scope.getProjectTypes();
			}, function (errorResponse) {
				$scope.showRedMessage(errorResponse);
			});

			$scope.generateProjectCode = function() {
				if ($scope.currentProject.name.length > 0) {
					$scope.currentProject.code = $scope.currentProject.name.replace(/[aeiou\s]/ig, '');
				}
			}
		}
	]
);
