'use strict';

angular.module('mockengerClientComponents')
    .factory('httpProviderInterceptor', ['$q', '$location', '$cookies', '$injector',
        function($q, $location, $cookies, $injector) {
            return {
                'request': function(config) {
                    if ($cookies.get('accessToken') != null && $cookies.get('accessToken') != 'undefined') {
                        config.headers['Authorization'] = 'Bearer ' + $cookies.get('accessToken');
                    }
                    return config;
                },
                'responseError': function(response) {
                    if (response != null && response.status == 401) {
                        if (response.data.error == 'invalid_token') {
                            var msg = response.data.error_description;
                            if (msg.indexOf("Access token expired") >= 0 || msg.indexOf("Invalid access token") >= 0) {
                                var deferred = $q.defer();

                                $cookies.remove('accessToken');
                                $injector.get('tokenService').getRefreshToken(deferred);

                                return deferred.promise.then(function() {
                                    return $injector.get('$http')(response.config);
                                });
                            }
                        }
                    }
                    return $q.reject(response);
                }
            };
}]);
