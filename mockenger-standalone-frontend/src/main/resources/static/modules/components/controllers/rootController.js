'use strict';

angular.module('mockengerClientComponents')
    .controller('rootController', ['$scope', 'ENV', 'APP_VERSION', 'BUILD_DATE', function ($scope, ENV, APP_VERSION, BUILD_DATE) {

        $scope.app = {
            env : ENV,
            version: APP_VERSION,
            buildDate: BUILD_DATE
        }

    }]);
