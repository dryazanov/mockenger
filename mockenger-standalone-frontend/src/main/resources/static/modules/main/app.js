'use strict';

var module = angular.module('mockengerClientMainApp', [
    'mockengerClientComponents',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngAnimate',
    'ngRoute',
    'ngSanitize',
    'ngTouch']);

module.config(['$locationProvider','$routeProvider', function ($locationProvider, $routeProvider) {
    //$locationProvider.html5Mode(true);
    $routeProvider
        .when('/', {
            templateUrl: '/modules/main/views/mainView.html',
            controller: 'mainController',
            controllerAs: 'mainCtrl'
        })
        .when('/project/:projectId', {
            templateUrl: '/modules/main/views/projectView.html',
            controller: 'projectController',
            controllerAs: 'projectCtrl',
            resolve: {
                currentProject: ['$route', 'projectsService', function($route, projectsService) {
                    var projectId = $route.current.params.projectId;
                    projectsService.projectId = projectId;
                    return projectsService.get({projectId : projectId});
                }]
            }
        })
        .otherwise({
            redirectTo: '/'
        });
}]);


/**
 * A generic confirmation for risky actions.
 * Usage: Add attributes: ng-really-message="Are you sure"? ng-really-click="takeAction()" function
 */
module.directive('ngReallyClick', [function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('click', function() {
                var message = attrs.ngReallyMessage;
                if (message && confirm(message)) {
                    scope.$apply(attrs.ngReallyClick);
                }
            });
        }
    }
}]);

