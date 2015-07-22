'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'apiEndpointsService', function ($scope, apiEndpointsService) {
        $scope.test = apiEndpointsService.doSomethingUseful();
    }]);
