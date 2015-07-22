'use strict';

angular.module('mockengerClientMainApp').factory('projectsService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {

    var Project = $resource(apiEndpointsService.getProjectRestUrl(),
        {projectId: '@projectId'}, {}
    );


    return Project;

}]);
