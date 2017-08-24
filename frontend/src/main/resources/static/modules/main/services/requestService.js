'use strict';

angular.module('mockengerClientMainApp').factory('requestService', ['$resource', 'apiEndpointsService', function($resource, apiEndpointsService) {

    var RequestService = {
        ajax: $resource(apiEndpointsService.getRequestRestUrl(), {
                requestCode: '@requestCode',
                groupCode: '@groupCode',
                projectCode: '@projectCode'
            }, {
                update: {
                    method:'PUT'
                }
            }
        )
    };

    return RequestService;
}]);
