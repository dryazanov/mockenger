'use strict';

angular.module('mockengerClientMainApp').factory('groupsService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {
    var Group = $resource(apiEndpointsService.getGroupRestUrl(),
        {
            groupId: '@groupId',
            projectId: '@projectId'
        }, {}
    );

    return Group;

}])
;
