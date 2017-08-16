'use strict';

angular.module('mockengerClientMainApp')
    .controller('EventListController', ['$scope', 'eventListService',
        function($scope, eventListService) {
            $scope.eventListService = eventListService;

            // Get event list
            $scope.updateEventList = function(types, page, sort) {
                eventListService.events.get({types: eventListService.getEntityType(), page: page, sort: sort}, function(response) {
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
}]);