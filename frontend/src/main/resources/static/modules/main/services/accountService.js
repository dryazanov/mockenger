'use strict';

angular.module('mockengerClientMainApp').factory('accountService', ['$rootScope', '$resource', 'apiEndpointsService',
    function($rootScope, $resource, apiEndpointsService) {
        var AccountService = {
            currentAccount: null,

            getCurrentAccount: function() {
                return AccountService.currentAccount;
            },

            setCurrentAccount: function(account) {
                AccountService.currentAccount = account;
            },

            ajax: $resource(apiEndpointsService.getAccountRestUrl(), {
                    accountId: '@accountId'
                }, {
                    update: {
                        method:'PUT'
                    }
                }
            ),

            openAccountModal: function(account) {
                if (account == null) {
                    account = {
                        id: null,
                        firstName: null,
                        lastName: null,
                        role: 'USER',
                        username: null
                    };
                }
                $rootScope.$broadcast('openAccountModal', account);
            }
        };

        return AccountService;
}]);
