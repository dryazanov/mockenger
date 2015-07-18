'use strict';

angular.module('mockengerClientMainApp')
    .controller('mainController',['$scope', 'testService', function ($scope, testService) {
        $scope.test = testService.doSomethingUseful();
    }]);
