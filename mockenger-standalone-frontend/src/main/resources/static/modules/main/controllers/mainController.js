'use strict';

angular.module('mockengerClientMainApp').controller('mainController', ['$scope', 'projectsService', function ($scope, projectsService) {

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

    $scope.getProjects = function() {
        projectsService.query(function(response, getResponseHeaders) {
            $scope.projectsList = response;
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    }

    $scope.createProject = function() {
        $scope.modalTitle = "Create project";
        $scope.currentProject = null;
        projectModal.modal({});
    };

    $scope.editProject = function(project) {
        $scope.modalTitle = "Edit project";
        $scope.currentProject = project;
        projectModal.modal({});
    };

    $scope.deleteProject = function(project) {
        projectsService.delete({projectId: project.id}, function(response, getResponseHeaders) {
            $scope.getProjects();
        }, function(errorResponse) {
            showErrors(errorResponse);
        });
    };

    $scope.saveProject = function(currentProject) {
        projectModal.modal('hide');
        if (currentProject.id != null) {
            projectsService.update({projectId: currentProject.id}, currentProject, function(response, getResponseHeaders) {
                $scope.getProjects();
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        } else {
            projectsService.save(currentProject, function() {
                $scope.getProjects();
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        }
    };

    $scope.getProjects();
}]);
