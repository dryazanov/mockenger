'use strict';

angular.module('mockengerClientMainApp').factory('valuesetService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {
    return $resource(apiEndpointsService.getValuesetRestUrl(), { id: '@id' }, {});
}]);
