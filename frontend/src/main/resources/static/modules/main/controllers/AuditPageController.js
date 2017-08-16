'use strict';

angular.module('mockengerClientMainApp')
    .controller('AuditPageController',['$scope', 'accountService', 'accountListService', 'eventListService', 'valuesetService', 'API_BASE_PATH',
        function ($scope, accountService, accountListService, eventListService, valuesetService, API_BASE_PATH) {

            $scope.eventTypes = {};
            $scope.eventEntityTypes = {};

            $scope.getEventTypes = function() {
                valuesetService.eventTypes.get(function(response, getResponseHeaders) {
                    $scope.eventTypes = response;
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.getEventEntityTypes = function() {
                valuesetService.eventEntityTypes.get(function(response, getResponseHeaders) {
                    if (response.length > 0) {
                        $scope.eventEntityTypes = response[0];
                    }
                }, function(errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.loadEventsByEntityType = function(type) {
                var page = 0;
                var sort = "";
                eventListService.events.get({types: type, page: page, sort: sort}, function(response) {
                    eventListService.setCurrentPage(page);
                    eventListService.setEntityType(type);
                    eventListService.setData(response);
                }, function (errorResponse) {
                    $scope.showRedMessage(errorResponse);
                });
            }

            $scope.getEventTypes();
            $scope.getEventEntityTypes();

}]);