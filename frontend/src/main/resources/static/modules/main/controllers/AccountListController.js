'use strict';

angular.module('mockengerClientMainApp')
    .controller('AccountListController', ['$scope', '$confirm', 'accountListService',
        function($scope, $confirm, accountListService) {
            $scope.accountListService = accountListService;

            // Get account list
            $scope.updateAccountList = function() {
                accountListService.ajax.query(function(response) {
                    accountListService.setData(response);
                }, function (errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.deleteAccount = function(account) {
                $confirm({
                    text: "Do you really want to delete account '" + account.username + "'?"
                }).then(function() {
                    accountListService.ajax.delete({accountId: account.id}, function(response, getResponseHeaders) {
                        $scope.updateAccountList();
                        $scope.showGreenMessage('Account <b>' + account.username + '</b> has been deleted');
                    }, function(errorResponse) {
                        $scope.showRedMessage(errorResponse);
                    });
                }, function() {
					// cancel
				});
            }

            $scope.updateAccountList();
}]);