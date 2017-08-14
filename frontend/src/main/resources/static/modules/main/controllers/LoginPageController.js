'use strict';

angular.module('mockengerClientMainApp')
    .controller('LoginPageController', ['$scope', '$cookies', '$location', 'loginService',
        function ($scope, $cookies, $location, loginService) {
            if ($cookies.get('accessToken') != null && $cookies.get('refreshToken') != null && $cookies.get('user')) {
                $location.path('/index');
            } else {
                loginService.showSignInForm($scope);
            }
        }
]);
