'use strict';

angular.module('mockengerClientMainApp')
	.controller('EventController', ['$scope', 'eventService',
		function($scope, eventService) {
			var eventModal = $('#eventModal');
			$scope.requestEvent = null;

			$scope.$on('openEventModal', function(event, requestEvent) {
				$scope.requestEvent = requestEvent;
				eventModal.modal({});
			});
}]);