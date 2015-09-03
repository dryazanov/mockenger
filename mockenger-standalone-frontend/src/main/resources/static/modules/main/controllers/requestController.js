'use strict';

angular.module('mockengerClientMainApp').controller('RequestController',['$scope', 'projectsService', 'groupService', 'requestService', 'requestsService', 'valuesetService',
    function ($scope, projectsService, groupService, requestService, requestsService, valuesetService) {

    $scope.selectMethod = function(method) {
        requestsService.getCurrent().method = method;
    }

    $scope.selectTransformerType = function(type, transformer) {
        transformer.type = type;
    }

    $scope.addParameter = function() {
        var source = requestsService.getCurrent().parameters.values;
        if (source == null) {
            source = [];
        }
        source.push({
            "key": "",
            "value": ""
        });
    }

    $scope.deleteParameter = function(index) {
        var source = requestsService.getCurrent().parameters.values;
        if (source != null && source[index] != null) {
            source.splice(index, 1);
        }
    }

    $scope.addRequestHeader = function() {
        if (requestsService.getCurrent().headers.values == null) {
            requestsService.getCurrent().headers.values = [];
        }
        addHeader(requestsService.getCurrent().headers.values);
    }

    $scope.addResponseHeader = function() {
        if (requestsService.getCurrent().mockResponse.headers == null) {
            requestsService.getCurrent().mockResponse.headers = [];
        }
        addHeader(requestsService.getCurrent().mockResponse.headers);
    }

    var addHeader = function(source) {
        source.push({
            "key": "",
            "value": ""
        });
    }

    $scope.deleteHeader = function(index, source) {
        if (source != null && source[index] != null) {
            source.splice(index, 1);
        }
    }

    // Add transformer for Path
    $scope.addPathTransformer = function() {
        if (requestsService.getCurrent().path.transformers == null) {
            requestsService.getCurrent().path.transformers = [];
        }
        addTransformer(requestsService.getCurrent().path.transformers);
    }

    // Add transformer for Parameters
    $scope.addParamTransformer = function() {
        if (requestsService.getCurrent().parameters.transformers == null) {
            requestsService.getCurrent().parameters.transformers = [];
        }
        addTransformer(requestsService.getCurrent().parameters.transformers);
    }

    // Add transformer for Headers
    $scope.addHeaderTransformer = function() {
        if (requestsService.getCurrent().headers.transformers == null) {
            requestsService.getCurrent().headers.transformers = [];
        }
        addTransformer(requestsService.getCurrent().headers.transformers);
    }

    // Add transformer for Request Body
    $scope.addRequestBodyTransformer = function() {
        if (requestsService.getCurrent().body.transformers == null) {
            requestsService.getCurrent().body.transformers = [];
        }
        addTransformer(requestsService.getCurrent().body.transformers);
    }

    var addTransformer = function(source) {
        source.push({
            type: 'REGEXP',
            pattern: null,
            replacement: null
        });
    }

    $scope.deleteTransformer = function(index, source) {
        if (source != null && source[index] != null) {
            source.splice(index, 1);
        }
    }
}]);
