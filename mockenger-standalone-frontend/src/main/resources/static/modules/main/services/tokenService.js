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
                    $http(params).success(successCallback).error(errorCallback);
                },

                getRefreshToken: function(deferred) {
                    var params = {
                        method: 'POST',
                        url: apiEndpointsService.getOAuth2RefreshTokenUrl() + "&refresh_token=" + $cookies.get('refreshToken'),
                        headers: headers
                    }
                    $http(params).success(function(data) {
                        $cookies.put('accessToken', data.access_token);
                        deferred.resolve();
                    }).error(function() {
                        $location.path('/login');
                        deferred.reject();
                    });
                }
            };

            return TokenService;
        }
    ]
);