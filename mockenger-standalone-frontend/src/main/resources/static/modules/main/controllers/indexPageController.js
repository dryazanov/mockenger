'use strict';

angular.module('mockengerClientMainApp').controller('IndexPageController', ['$scope', '$filter', 'projectListService', 'valuesetService',
    function ($scope, $filter, projectListService, valuesetService) {

        // Show alerts in case of error
        $scope.alerts = [];

        $scope.addAlert = function(message) {
            $scope.alerts.push({type: 'danger', msg: message});
        };

        $scope.closeAlert = function(index) {
            $scope.alerts = [];
        };

        var showErrors = function(errorResponse) {
            $scope.alerts = [];
            if (errorResponse.data != null && errorResponse.data.errors != null && errorResponse.data.errors.length > 0) {
                var errors = errorResponse.data.errors;
                for (var idx in errors) {
                    $scope.alerts.push({type: 'danger', msg: errors[idx]});
                }
            }
        }



        $scope.projectListService = projectListService;

        $scope.projectsList = {};
        $scope.availableProjectTypes = {};
        var projectModal = $('#projectModal');

        $scope.$watch('currentProject.code', function(value) {
            if (value != null) {
                $scope.currentProject.code = $filter('uppercase')(value);
            }
        });

        $scope.getProjectTypes = function() {
            valuesetService.projectTypes.get(function(response, getResponseHeaders) {
                $scope.availableProjectTypes = response;
            }, function(errorResponse) {
                //showErrors(errorResponse);
            });
        }

        $scope.getProjects = function() {
            projectListService.ajax.query(function(response, getResponseHeaders) {
                projectListService.setData(response);
            }, function(errorResponse) {
                showErrors(errorResponse);
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
            projectListService.ajax.delete({projectId: project.id}, function(response, getResponseHeaders) {
                $scope.getProjects();
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        };

        $scope.saveProject = function(currentProject) {
            if (currentProject.id != null) {
                projectListService.ajax.update({projectId: currentProject.id}, currentProject, function(response, getResponseHeaders) {
                    projectModal.modal('hide');
                    $scope.getProjects();
                }, function(errorResponse) {
                    showErrors(errorResponse);
                });
            } else {
                projectListService.ajax.save(currentProject, function() {
                    projectModal.modal('hide');
                    $scope.getProjects();
                }, function(errorResponse) {
                    showErrors(errorResponse);
                });
            }
        };

        $scope.getProjectTypes();
        $scope.getProjects();
    }
]);
