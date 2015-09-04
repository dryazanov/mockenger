'use strict';

angular.module('mockengerClientMainApp').controller('RequestController',['$scope', 'ngToast', 'projectListService', 'groupListService', 'requestService', 'requestListService', 'valuesetService',
    function ($scope, ngToast, projectListService, groupListService, requestService, requestListService, valuesetService) {

    $scope.selectMethod = function(method) {
        requestListService.getCurrent().method = method;
    }

    $scope.selectTransformerType = function(type, transformer) {
        transformer.type = type;
    }

    $scope.addParameter = function() {
        if (requestListService.getCurrent().parameters.values == null) {
            requestListService.getCurrent().parameters.values = [];
        }
        requestListService.getCurrent().parameters.values.push({
            "key": "",
            "value": ""
        });
    }

    $scope.deleteParameter = function(index) {
        var source = requestListService.getCurrent().parameters.values;
        if (source != null && source[index] != null) {
            source.splice(index, 1);
        }
    }

    $scope.addRequestHeader = function() {
        if (requestListService.getCurrent().headers.values == null) {
            requestListService.getCurrent().headers.values = [];
        }
        addHeader(requestListService.getCurrent().headers.values);
    }

    $scope.addResponseHeader = function() {
        if (requestListService.getCurrent().mockResponse == null) {
            requestListService.getCurrent().mockResponse = {
                headers: [],
                httpStatus: null,
                body: null
            }
        } else {
            if (requestListService.getCurrent().mockResponse.headers == null) {
                requestListService.getCurrent().mockResponse.headers = [];
            }
        }
        addHeader(requestListService.getCurrent().mockResponse.headers);
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
        if (requestListService.getCurrent().path.transformers == null) {
            requestListService.getCurrent().path.transformers = [];
        }
        addTransformer(requestListService.getCurrent().path.transformers);
    }

    // Add transformer for Parameters
    $scope.addParamTransformer = function() {
        if (requestListService.getCurrent().parameters.transformers == null) {
            requestListService.getCurrent().parameters.transformers = [];
        }
        addTransformer(requestListService.getCurrent().parameters.transformers);
    }

    // Add transformer for Headers
    $scope.addHeaderTransformer = function() {
        if (requestListService.getCurrent().headers.transformers == null) {
            requestListService.getCurrent().headers.transformers = [];
        }
        addTransformer(requestListService.getCurrent().headers.transformers);
    }

    // Add transformer for Request Body
    $scope.addRequestBodyTransformer = function() {
        if (requestListService.getCurrent().body.transformers == null) {
            requestListService.getCurrent().body.transformers = [];
        }
        addTransformer(requestListService.getCurrent().body.transformers);
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

    $scope.saveRequest = function(currentRequest) {
        var requestParams = {
            projectId: projectListService.getCurrent().id,
            groupId: groupListService.getCurrent().id
        }
        if (currentRequest.id != null) {
            requestParams.requestId = currentRequest.id;
            requestService.ajax.update(requestParams, currentRequest, function(response) {
                ngToast.create('Request <b>' + currentRequest.name + '</b> has been saved');
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        } else {
            requestService.ajax.save(requestParams, currentRequest, function() {
                ngToast.create('New request has been created');
            }, function(errorResponse) {
                showErrors(errorResponse);
            });
        }
    }
}]);
