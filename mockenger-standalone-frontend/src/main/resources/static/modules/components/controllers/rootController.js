'use strict';

angular.module('mockengerClientComponents')
    .controller('rootController', ['$scope', 'ENV', 'APP_VERSION', 'BUILD_DATE', function ($scope, ENV, APP_VERSION, BUILD_DATE) {

        $scope.app = {
            env: ENV,
            version: APP_VERSION,
            buildDate: BUILD_DATE
        };

        var STATUS = {
            IDLE: 0,
            LOADING: 1,
            ERROR: 2
        };

        $scope.setContentLoading = function () {
            return STATUS.LOADING;
        };

        $scope.setContentReady = function () {
            return STATUS.IDLE;
        };

        $scope.setContentLoadingFailed = function () {
            return STATUS.ERROR;
        };

        $scope.isContentLoading = function (status) {
            return status === STATUS.LOADING;
        };

        $scope.isContentReady = function (status) {
            return status === STATUS.IDLE;
        };

        $scope.isContentLoadingFailed = function (status) {
            return status === STATUS.ERROR;
        };

        $scope.hasError = function(input) {
            return (input.$invalid && !input.$pristine ? 'has-error' : '');
        }


    }]);
