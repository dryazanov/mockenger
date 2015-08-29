'use strict';

angular.module('mockengerClientMainApp').factory('groupsService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {

    var GroupService = {
        data: null,
        current: null,

        getData: function() {
            return GroupService.data;
        },

        setData: function(dataToSet) {
            GroupService.data = dataToSet;
        },

        getCurrent: function() {
            return GroupService.current;
        },

        setCurrent: function(currentToSet) {
            GroupService.current = currentToSet;
        },

        ajax: $resource(apiEndpointsService.getGroupRestUrl(), {
                groupId: '@groupId',
                projectId: '@projectId'
            }, {}
        ),

        addGroupToList: function(group) {
            if (GroupService.data == null) {
                GroupService.data = [];
            }
            GroupService.data.push(group);
        }
    };

    return GroupService;
}]);
