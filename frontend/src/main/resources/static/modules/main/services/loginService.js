'use strict';

angular.module('mockengerClientMainApp').factory('loginService', ['$uibModal', function($uibModal) {

    var LoginService = {
        showSignInForm: function(scope) {
            $uibModal.open({
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
