'use strict';

angular.module('mockengerClientMainApp').factory('requestsService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {

    var Request = $resource(apiEndpointsService.getRequestRestUrl(),
        {
            groupId: '@groupId',
            projectId: '@projectId',
            requestId: '@requestId'
        }, {}
    );

    return Request;

}]);
