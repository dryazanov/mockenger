'use strict';

angular.module('mockengerClientMainApp').factory('loginService', ['$modal', function($modal) {

    var LoginService = {
        showSignInForm: function(scope) {
            $modal.open({
                templateUrl: '/modules/main/views/loginForm.html',
                controller: 'LoginController',
                backdrop : 'static',
                keyboard: false,
                scope: scope
            });
        }
    };

    return LoginService;
}]);
