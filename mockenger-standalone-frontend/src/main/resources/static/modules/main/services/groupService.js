'use strict';

angular.module('mockengerClientMainApp').factory('groupService', ['$rootScope', '$resource', 'apiEndpointsService', function($rootScope, $resource, apiEndpointsService) {

    var GroupService = {
        ajax: $resource(apiEndpointsService.getGroupRestUrl(), {
                groupId: '@groupId',
                projectId: '@projectId'
            }, {
                update: {
                    method:'PUT'
                }
            }
        ),

        openGroupModal: function(project, group) {
            if (group == null) {
                group = {
                    groupId: null,
                    projectId: project.id,
                    name: null,
                    recording: true
                };
            }
            $rootScope.$broadcast('openGroupModal', group);
        }
    };

    return GroupService;
}]);
