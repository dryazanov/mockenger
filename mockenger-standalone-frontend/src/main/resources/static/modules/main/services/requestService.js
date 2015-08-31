'use strict';

angular.module('mockengerClientMainApp').factory('requestService', ['$resource', 'apiEndpointsService', function($resource, apiEndpointsService) {

    var RequestService = {
        ajax: $resource(apiEndpointsService.getGroupRestUrl(), {
                requestId: '@requestId',
                groupId: '@groupId',
                projectId: '@projectId'
            }, {
                'update': {
                    method:'PUT'
                }
            }
        )
    };

    return RequestService;
}]);
