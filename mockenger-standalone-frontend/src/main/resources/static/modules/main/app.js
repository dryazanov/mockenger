'use strict';

angular.module('mockengerClientMainApp', [
    'mockengerClientComponents',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch']).config(['$routeProvider', function ($routeProvider) {
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
                    return projectsService.get({projectId : projectId});
                }]
            }
        })
        .otherwise({
            redirectTo: '/'
        });
}]);


