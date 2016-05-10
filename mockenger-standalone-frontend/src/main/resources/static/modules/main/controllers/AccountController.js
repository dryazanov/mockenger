'use strict';

angular.module('mockengerClientMainApp')
    .controller('AccountController', ['$scope', 'accountService', 'accountListService',
        function($scope, accountService, accountListService) {

            var accountModal = $('#accountModal');
            $scope.currentAccount = {};
            $scope.repassword = "";

            $scope.$on('openAccountModal', function(event, account) {
                angular.copy(account, $scope.currentAccount);
                $scope.accountForm.$setPristine();
                accountModal.modal({});
            });

            $scope.saveAccount = function(account) {
                account.password = account.password || "";
                repassword.value = repassword.value || "";

                if (account.password !== repassword.value) {
                    $scope.accountForm.repassword.$invalid = true;
                } else {
                    $scope.accountForm.repassword.$invalid = false;

                    if (account.id != null) {
                        $scope.updateAccountRequest(account, {accountId: account.id}, 'Account <b>' + account.username + '</b> successfully updated');
                    } else {
                        $scope.saveAccountRequest(account, 'Account <b>' + account.username + '</b> has been created');
                    }
                }
            };

            $scope.updateAccountRequest = function(account, requestParams, greenMessage) {
                accountService.ajax.update(requestParams, account, function() {
                    accountModal.modal('hide');
                    $scope.showGreenMessage(greenMessage);
                    $scope.getAccountList();
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.saveAccountRequest = function(account, greenMessage) {
                accountService.ajax.save(account, function() {
                    accountModal.modal('hide');
                    $scope.showGreenMessage(greenMessage);
                    $scope.getAccountList();
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.getAccountList = function() {
                accountListService.ajax.query(function(response) {
                    accountListService.setData(response);
                }, function (errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }
}]);