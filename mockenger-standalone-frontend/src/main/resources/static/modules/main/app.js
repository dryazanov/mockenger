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
    'ui.bootstrap',
    'ngToast']).config(['$locationProvider', '$routeProvider', 'ngToastProvider', function ($locationProvider, $routeProvider, ngToastProvider) {
    //$locationProvider.html5Mode(true);
    $routeProvider.when('/', {
        templateUrl: '/modules/main/views/indexView.html',
        controller: 'IndexPageController',
        resolve: {
            projectList: ['$route', 'projectListService', function($route, projectListService) {
                return projectListService.ajax.query();
            }]
        }
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
    .otherwise({
        redirectTo: '/'
    });

    ngToastProvider.configure({
        animation: 'fade', // or 'fade'
        horizontalPosition: 'center',
        dismissButton: true,
        timeout: 4000
    });
}]);


/**
 * A generic confirmation for risky actions.
 * Usage: Add attributes: ng-really-message="Are you sure"? ng-really-click="takeAction()" function
 */
angular.module('mockengerClientMainApp').directive('ngReallyClick', [function() {
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

