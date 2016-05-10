'use strict';

angular.module('mockengerClientMainApp')
    .factory('eventListService', ['$resource', 'apiEndpointsService',
        function ($resource, apiEndpointsService) {

            var methodGet = {
                get: {
                    method:'GET',
                    isArray: true
                }
            }

            var EventListService = {
                data: null,
                current: 0,

                getCurrentPage: function() {
                    return EventListService.current;
                },

                setCurrentPage: function(n) {
                    EventListService.current = n;
                },

                getData: function() {
                    return EventListService.data;
                },

                setData: function(dataToSet) {
                    EventListService.data = dataToSet;
                },

                events: $resource(apiEndpointsService.getEventRestUrl(), {eventId: '@eventId'}, {}, methodGet),
            };

            return EventListService;
}]);
