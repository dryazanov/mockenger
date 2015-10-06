'use strict';

angular.module('mockengerClientMainApp')
    .controller('LoginController', ['$http', '$cookies', '$scope', '$location', 'apiEndpointsService', 'tokenService',
        function($http, $cookies, $scope, $location, apiEndpointsService, tokenService) {
            $scope.username = "";
            $scope.password = "";

            $scope.doSignIn = function() {
                tokenService.getAccessToken($scope.username, $scope.password, function(data, status, headers, config) {
                    $cookies.put('accessToken', data.access_token);
                    $cookies.put('refreshToken', data.refresh_token);

                    $http.get(apiEndpointsService.getUserData()).success(function(data) {
                        $cookies.put('user', angular.toJson(data));
                        $location.path('/index');
                    }).error(function(data, status, headers, config) {
                        $scope.showRedMessage({data: data});
                    });
                }, function(data, status, headers, config) {
                    $scope.showRedMessage({data: data});
                });
            }
        }
]);
