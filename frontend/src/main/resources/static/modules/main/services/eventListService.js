'use strict';

angular.module('mockengerClientMainApp')
	.factory('eventListService', ['$resource', 'apiEndpointsService',
		function ($resource, apiEndpointsService) {

			var methodGet = {
				get: {
					method:'GET',
					isArray: false
				}
			}

			var EventListService = {
				data: null,
				current: 0,
				entityType: null,

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

				getEntityType: function() {
					return EventListService.entityType;
				},

				setEntityType: function(entityType) {
					EventListService.entityType = entityType;
				},

				events: $resource(apiEndpointsService.getEventRestUrl(), {eventId: '@eventId'}, {}, methodGet),
			};

			return EventListService;
}]);
