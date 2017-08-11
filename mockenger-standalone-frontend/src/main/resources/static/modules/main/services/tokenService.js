'use strict';

angular.module('mockengerClientMainApp')
    .factory('tokenService', ['$injector', '$cookies', '$location', 'apiEndpointsService', 'SECRET_KEY',
        function($injector, $cookies, $location, apiEndpointsService, SECRET_KEY) {

            var $http = $injector.get('$http');
            var headers = {
                'Content-type': 'application/x-www-form-urlencoded',
                'Authorization': 'Basic ' + SECRET_KEY
            };

            var TokenService = {
                getAccessToken: function(username, password, successCallback, errorCallback) {
                    var params = {
                        method: 'POST',
                        url: apiEndpointsService.getOAuth2AccessTokenUrl(),
                        data: "username=" + username + "&password=" + password,
                        headers: headers
                    };
                    $http(params)
						.then(function onSuccess(response) {
							successCallback(response.data);
						}, function onError(response) {
							errorCallback(response.data);
						});
                },

                getRefreshToken: function(deferred) {
                    var params = {
                        method: 'POST',
                        url: apiEndpointsService.getOAuth2RefreshTokenUrl() + "&refresh_token=" + $cookies.get('refreshToken'),
                        headers: headers
                    }
                    $http(params)
                   		.then(function onSuccess(response) {
							$cookies.put('accessToken', response.data.access_token);
							deferred.resolve();
						}, function onError(response) {
							$location.path('/login');
							deferred.reject();
						});
                }
            };

            return TokenService;
        }
    ]
);