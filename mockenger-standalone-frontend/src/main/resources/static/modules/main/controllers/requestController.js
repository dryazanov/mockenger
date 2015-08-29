'use strict';

angular.module('mockengerClientMainApp').controller('RequestController',['$scope', 'requestsService', function ($scope, requestsService) {

    $scope.updateCurrent = function() {
        requestsService.getCurrent().path.value = 'newvalue';
    }

    var REQUEST_VIEW = {
        HEADERS: 'HEADERS',
        PARAMETERS: 'PARAMETERS',
        BODY: 'BODY'
    };

    $scope.setHeadersRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.HEADERS;
    };

    $scope.setParamsRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.PARAMETERS;
    };

    $scope.setBodyRequestView = function() {
        $scope.currentRequestView = REQUEST_VIEW.BODY;
    };

    $scope.isHeadersRequestViewActive = function() {
      return $scope.currentRequestView === REQUEST_VIEW.HEADERS;
    };

    $scope.isBodyRequestViewActive = function() {
        return $scope.currentRequestView === REQUEST_VIEW.BODY;
    };

    $scope.isParamsRequestViewActive = function() {
        return $scope.currentRequestView === REQUEST_VIEW.PARAMETERS;
    };
}]);
