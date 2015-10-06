'use strict';

angular.module('mockengerClientMainApp')
    .controller('LogoutController', ['$scope', '$http', '$cookies', '$location', 'apiEndpointsService',
        function($scope, $http, $cookies, $location, apiEndpointsService) {
            $http.post(apiEndpointsService.getOAuth2LogoutUrl() + "?token=" + $cookies.get('accessToken'), null).success(function() {
                $cookies.remove('accessToken');
                $cookies.remove('refreshToken');
                $cookies.remove('user');

                $location.path("/login");
            }).error(function(data, status, headers, config) {
                $scope.showRedMessage({data: data});
            });
    }
]);
