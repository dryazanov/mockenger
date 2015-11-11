'use strict';

angular.module('mockengerClientMainApp')
    .controller('AdminPageController',['$scope', 'accountService', 'accountListService', 'valuesetService', 'API_BASE_PATH',
        function ($scope, accountService, accountListService, valuesetService, API_BASE_PATH) {

            $scope.roles = {};

            $scope.createAccount = function() {
                accountService.openAccountModal(null);
            };

            $scope.editAccount = function(account) {
                accountService.openAccountModal(account);
            };

            $scope.getRoles = function() {
                valuesetService.roles.get(function(response, getResponseHeaders) {
                    $scope.roles = response;
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.getRoles();

}]);