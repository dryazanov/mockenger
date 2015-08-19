'use strict';

angular.module('mockengerClientMainApp').controller('mainController', ['$scope', '$filter', 'projectsService', function ($scope, $filter, projectsService) {

    $scope.alerts = [];

    $scope.addAlert = function(message) {
        $scope.alerts.push({type: 'danger', msg: message});
    };

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    var showErrors = function(errorResponse) {
        if (errorResponse.data != null && errorResponse.data.errors != null && errorResponse.data.errors.length > 0) {
            var errors = errorResponse.data.errors;
            for (var idx in errors) {
                $scope.alerts.push({type: 'danger', msg: errors[idx]});
            }
        }
    }



    $scope.modalTitle = "";
    $scope.projectsList = {};
    var projectModal = $('#projectModal');

    $scope.$watch('currentProject.code', function(value) {
        if (value != null) {
            $scope.currentProject.code = $filter('uppercase')(value);
        }
    });

    $scope.getProjects = function() {
        projectsService.query(function(response, getResponseHeaders) {
            $scope.projectsList = response;
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    }

    $scope.createProject = function() {
        $scope.modalTitle = "Create project";
        $scope.currentProject = {};
        $scope.projectForm.$setPristine();
        projectModal.modal({});
    };

    $scope.editProject = function(project) {
        $scope.modalTitle = "Edit project";

        projectsService.get({projectId: project.id}, function(response, getResponseHeaders) {
            $scope.currentProject = response;
            projectModal.modal({});
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    };

    $scope.deleteProject = function(project) {
        projectsService.delete({projectId: project.id}, function(response, getResponseHeaders) {
            $scope.getProjects();
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    };

    $scope.saveProject = function(currentProject) {
        if (currentProject.id != null) {
            projectsService.update({projectId: currentProject.id}, currentProject, function(response, getResponseHeaders) {
                projectModal.modal('hide');
                $scope.getProjects();
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        } else {
            projectsService.save(currentProject, function() {
                projectModal.modal('hide');
                $scope.getProjects();
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        }
    };

    $scope.getProjects();
}]);
