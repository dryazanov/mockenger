'use strict';

angular.module('mockengerClientMainApp').controller('RequestController',['$scope', 'requestService', 'requestsService', 'valuesetService',
    function ($scope, requestService, requestsService, valuesetService) {

    $scope.requestMethods = {};
    $scope.transformerTypes = {};

    $scope.getRequestMethods = function() {
        valuesetService.get({id: "request-method"}, function(response, getResponseHeaders) {
            $scope.requestMethods = response;
        }, function(errorResponse) {
            //showErrors(errorResponse);
        });
    }

    $scope.getTransformerTypes = function() {
        valuesetService.get({id: "transformer-type"}, function(response, getResponseHeaders) {
            $scope.transformerTypes = response;
        }, function(errorResponse) {
            //showErrors(errorResponse);
        });
    }

    $scope.selectMethod = function(method) {
        requestsService.getCurrent().method = method;
    }

    $scope.selectTransformerType = function(type, transformer) {
        transformer.type = type;
    }

    /*$scope.addParameter = function() {
        var length = 1;
        var source = requestsService.getCurrent().parameters.values;
        if (source == null) {
            source = {};
        } else {
            length = Object.keys(source).length;
        }
        source['param' + (length + 1)] = "";
    }*/

    $scope.addTransformer = function(source) {
        if (source == null) {
            source = [];
        }
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

    $scope.getRequestMethods();
    $scope.getTransformerTypes();
}]);
