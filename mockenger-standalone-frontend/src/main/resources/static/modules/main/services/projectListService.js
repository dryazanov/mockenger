'use strict';

angular.module('mockengerClientMainApp').factory('projectListService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {

    var ProjectService = {
        data: null,
        current: null,

        getData: function() {
            return ProjectService.data;
        },

        setData: function(dataToSet) {
            ProjectService.data = dataToSet;
        },

        getCurrent: function() {
            return ProjectService.current;
        },

        setCurrent: function(currentToSet) {
            ProjectService.current = currentToSet;
        },

        ajax: $resource(apiEndpointsService.getProjectRestUrl(), {
                projectId: '@projectId'
            }, {
                'update': {
                    method:'PUT'
                }
            }
        )
    };

    return ProjectService;
}]);
