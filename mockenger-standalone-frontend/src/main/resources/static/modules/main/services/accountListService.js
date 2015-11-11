'use strict';

angular.module('mockengerClientMainApp')
    .factory('accountListService', ['$resource', 'apiEndpointsService',
        function ($resource, apiEndpointsService) {

            var AccountListService = {
                data: null,

                getData: function() {
                    return AccountListService.data;
                },

                setData: function(dataToSet) {
                    AccountListService.data = dataToSet;
                },

                ajax: $resource(apiEndpointsService.getAccountRestUrl(), {accountId: '@accountId'}, {}),
            };

            return AccountListService;
}]);
