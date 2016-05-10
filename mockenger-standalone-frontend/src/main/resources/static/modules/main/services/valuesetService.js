'use strict';

angular.module('mockengerClientMainApp')
    .factory('valuesetService', ['$resource', 'apiEndpointsService',
        function ($resource, apiEndpointsService) {

            var methodGet = {
                get: {
                    method:'GET',
                    isArray: true
                }
            }

            var ValueSetService = {
                projectTypes: $resource(apiEndpointsService.getValuesetRestUrl.projectTypes(), {}, methodGet),
                requestMethods: $resource(apiEndpointsService.getValuesetRestUrl.requestMethods(), { projectId: '@projectId' }, methodGet),
                transformerTypes: $resource(apiEndpointsService.getValuesetRestUrl.transformerTypes(), {}, methodGet),
                headers: $resource(apiEndpointsService.getValuesetRestUrl.headers(), {}, methodGet),
                roles: $resource(apiEndpointsService.getValuesetRestUrl.roles(), {}, methodGet)
            }

            return ValueSetService;
}]);
