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
            controllerAs: 'main'
        })
        //.when('/about', {
        //    templateUrl: 'views/about.html',
        //    controller: 'AboutCtrl',
        //    controllerAs: 'about'
        //})
        .otherwise({
            redirectTo: '/'
        });
});


