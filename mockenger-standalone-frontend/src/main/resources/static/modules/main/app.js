'use strict';

angular.module('mockengerClientMainApp', [
    'mockengerClientComponents',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch']).config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: '/modules/main/views/mainView.html',
            controller: 'mainController',
            controllerAs: 'mainCtrl'
        })
        .when('/project/:projectId', {
            templateUrl: '/modules/main/views/projectView.html',
            controller: 'projectController',
            controllerAs: 'projectCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
});


