'use strict';

angular.module('mockengerClientMainApp')
    .controller('EventListController', ['$scope', 'eventService', 'eventListService',
        function($scope, eventService, eventListService) {
            $scope.eventService = eventService;
            $scope.eventListService = eventListService;
            eventListService.setData(null);

            // Get event list
            $scope.updateEventList = function(page) {
            	var params = {
            		types: eventListService.getEntityType(),
            		startDate: moment($scope.dp.dateRangeStart || 0).valueOf(),
            		endDate: moment($scope.dp.dateRangeEnd || new Date).valueOf(),
            		page: page
            	};

                eventListService.events.get(params, function(response) {
                    eventListService.setCurrentPage(page);
                    eventListService.setData(response);
                }, function (errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            };

            $scope.pageCount = function() {
                if (eventListService.getData() != null) {
                    return eventListService.getData().totalPages;
                }
                return 0;
            };

            $scope.range = function() {
                var range = [];
                for (var i = 0, l = $scope.pageCount(); i < l; i++) {
                    range.push(i);
                }
                return range;
            };

            $scope.isActive = function(n) {
                return (eventListService.getCurrentPage() == n);
            };

            $scope.isPrevPageDisabled = function() {
                return (eventListService.getCurrentPage() === 0);
            };

            $scope.isNextPageDisabled = function() {
                return (eventListService.getCurrentPage() === $scope.pageCount() - 1);
            };

            $scope.prevPage = function() {
                if (eventListService.getCurrentPage() > 0) {
                    eventListService.setCurrentPage(eventListService.getCurrentPage() - 1);
                    $scope.updateEventList(eventListService.getCurrentPage());
                }
            };

            $scope.nextPage = function() {
                if (eventListService.getCurrentPage() < $scope.pageCount() - 1) {
                    eventListService.setCurrentPage(eventListService.getCurrentPage() + 1);
                    $scope.updateEventList(eventListService.getCurrentPage());
                }
            };

            $scope.showFullEvent = function(event, index) {
				eventService.openEventModal(event);
			}

            $scope.getParametersAsString = function(params) {
				var result = "";

				if (params != null && params.values != null) {
					for (var i = 0, l = params.values.length; i < l; i++) {
						result += " '" + params.values[i].key + "=" + params.values[i].value + "',";
					}
				}

				return result;
			}

            $scope.getHeadersAsString = function(headers) {
				var result = "";

				if (headers != null && headers.values != null) {
					for (var i = 0, l = headers.values.length; i < l; i++) {
						result += " '" + headers.values[i].key + ": " + headers.values[i].value + "',";
					}
				}

				return result;
			}


			$scope.cleanSearchFilter = function() {
				$scope.dp.dateRangeStart = null;
				$scope.dp.dateRangeEnd = null;
				$scope.updateEventList(0);
			}


			// DATE PICKERS

            moment.locale('en', {
            	longDateFormat : {
					LT: "HH:mm",
					LTS: "HH:mm:ss"}
            	}
            );

			$scope.dp = {
				dateRangeStart: null,
				dateRangeEnd: null
			};

			$scope.endDateBeforeRender = function($view, $dates) {
				if ($scope.dp.dateRangeStart) {
					var activeDate = moment($scope.dp.dateRangeStart).subtract(1, $view).add(1, 'minute');

					$dates.filter(function (date) {
						return date.localDateValue() <= activeDate.valueOf()
					}).forEach(function (date) {
						date.selectable = false;
					});
				}
			}

            $scope.startDateBeforeRender = function($dates) {
				if ($scope.dp.dateRangeEnd) {
					var activeDate = moment($scope.dp.dateRangeEnd);

					$dates.filter(function (date) {
						return date.localDateValue() >= activeDate.valueOf()
					}).forEach(function (date) {
						date.selectable = false;
					});
				}
			}

            $scope.startDateOnSetTime = function(newDate, oldDate) {
				$scope.$broadcast('start-date-changed');
			}

            $scope.endDateOnSetTime = function() {
				$scope.$broadcast('end-date-changed');
			}

}]);