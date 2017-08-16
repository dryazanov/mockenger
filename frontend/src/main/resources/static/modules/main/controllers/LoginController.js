'use strict';

angular.module('mockengerClientMainApp')
    .controller('LoginController', ['$http', '$cookies', '$scope', '$location', 'apiEndpointsService', 'tokenService',
        function($http, $cookies, $scope, $location, apiEndpointsService, tokenService) {
            $scope.username = "";
            $scope.password = "";

            $scope.doSignIn = function() {
                tokenService.getAccessToken($scope.username, $scope.password, function(data) {
                    $cookies.put('accessToken', data.access_token);
                    $cookies.put('refreshToken', data.refresh_token);

                    $http.get(apiEndpointsService.getUserData())
						.then(function onSuccess(response) {
							$cookies.put('user', angular.toJson(response.data));
							$location.path('/index');
						}, function onError(response) {
							$scope.showRedMessage({data: response.data});
						});
                }, function(data) {
                    $scope.showRedMessage({data: data});
                });
            }
        }
]);
