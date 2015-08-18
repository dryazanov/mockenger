'use strict';

angular.module('mockengerClientMainApp').factory('projectsService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {
    return $resource(
        apiEndpointsService.getProjectRestUrl(), {
            projectId: '@projectId'
        }, {
            'update': {
                method:'PUT'
            }
        }
    );
}]);
