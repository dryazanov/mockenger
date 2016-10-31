'use strict';

angular.module('mockengerClientMainApp', [
        'mockengerClientComponents',
        'ngCookies',
        'ngMessages',
        'ngResource',
        'ngAnimate',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ngToast',
        'ui.bootstrap',
        'angular-confirm',
        'ngProgress'
    ])
    .config(['$locationProvider', '$routeProvider', '$httpProvider', 'ngToastProvider', 'SECURITY',
        function ($locationProvider, $routeProvider, $httpProvider, ngToastProvider, SECURITY) {
            //$locationProvider.html5Mode(true);
            $routeProvider
                .when('/index', {
                    templateUrl: '/modules/main/views/indexView.html',
                    controller: 'IndexPageController'
                })
                .when('/project/:projectId', {
                    templateUrl: '/modules/main/views/projectView.html',
                    controller: 'ProjectPageController',
                    resolve: {
                        currentProject: ['$route', 'projectListService', function($route, projectListService) {
                            var projectId = $route.current.params.projectId;
                            projectListService.projectId = projectId;
                            return projectListService.ajax.get({projectId : projectId});
                        }]
                    }
                })
                .when('/audit', {
                    templateUrl: '/modules/main/views/auditView.html',
                    controller: 'AuditPageController'
                })
                .when('/admin', {
                    templateUrl: '/modules/main/views/adminView.html',
                    controller: 'AdminPageController'
                })
                .otherwise({
                    redirectTo: '/index'
                });

            if (String(SECURITY) == 'true') {
                $routeProvider
                    .when('/login', {
                        template: ' ',
                        controller: 'LoginPageController'
                    })
                    .when('/logout', {
                        template: ' ',
                        controller: 'LogoutController'
                    })
                    .otherwise({
                        redirectTo: '/login'
                    });

                $httpProvider.interceptors.push('httpProviderInterceptor');
            }

            ngToastProvider.configure({
                animation: 'fade',
                horizontalPosition: 'center',
                dismissButton: true,
                timeout: 4000
            });
    }])
    .run(function($rootScope, $confirmModalDefaults, ngProgressFactory) {
        $rootScope.progressbar = ngProgressFactory.createInstance();
        $rootScope.progressbar.setHeight('10px');
        $confirmModalDefaults.templateUrl = '/modules/main/views/confirm.html';
    });