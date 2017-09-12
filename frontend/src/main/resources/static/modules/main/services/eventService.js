'use strict';

angular.module('mockengerClientMainApp')
	.factory('eventService', ['$rootScope', '$resource',
		function ($rootScope, $resource) {
			return {
				openEventModal: function(event) {
					$rootScope.$broadcast('openEventModal', event);
				}
			};
}]);
