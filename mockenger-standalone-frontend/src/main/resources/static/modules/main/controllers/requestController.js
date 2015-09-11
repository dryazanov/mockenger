'use strict';

angular.module('mockengerClientMainApp')
    .controller('RequestController',[
        '$scope',
        'projectListService',
        'groupListService',
        'requestService',
        'requestListService',
        'valuesetService',

        function ($scope, projectListService, groupListService, requestService, requestListService, valuesetService) {
            var REGEXP = 'REGEXP';
            var KEY_VALUE = 'KEY_VALUE';
            var XPATH = 'XPATH';

            $scope.bodyTransformerTypes = new Array(REGEXP, XPATH);

            var pushKeyValuePair = function(source) {
                source.push({
                    key: "",
                    value: ""
                });
            }

            var addTransformer = function(source, transformerType) {
                source.push({
                    type: transformerType,
                    pattern: null,
                    replacement: null
                });
            }

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
                pushKeyValuePair(requestListService.getCurrent().parameters.values);
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
                pushKeyValuePair(requestListService.getCurrent().headers.values);
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
                pushKeyValuePair(requestListService.getCurrent().mockResponse.headers);
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
                addTransformer(requestListService.getCurrent().path.transformers, REGEXP);
            }

            // Add transformer for Parameters
            $scope.addParamTransformer = function() {
                if (requestListService.getCurrent().parameters.transformers == null) {
                    requestListService.getCurrent().parameters.transformers = [];
                }
                addTransformer(requestListService.getCurrent().parameters.transformers, KEY_VALUE);
            }

            // Add transformer for Headers
            $scope.addHeaderTransformer = function() {
                if (requestListService.getCurrent().headers.transformers == null) {
                    requestListService.getCurrent().headers.transformers = [];
                }
                addTransformer(requestListService.getCurrent().headers.transformers, KEY_VALUE);
            }

            // Add transformer for Request Body
            $scope.addRequestBodyTransformer = function() {
                if (requestListService.getCurrent().body.transformers == null) {
                    requestListService.getCurrent().body.transformers = [];
                }
                addTransformer(requestListService.getCurrent().body.transformers, REGEXP);
            }

            $scope.deleteTransformer = function(index, source) {
                if (source != null && source[index] != null) {
                    source.splice(index, 1);
                }
            }

            $scope.saveRequest = function(request) {
                if (request.name == null || request.name == '') {
                    $scope.showRedMessage({data: {errors: new Array('Field <b>Name</b> is required')}});
                    return;
                }
                if (request.mockResponse == null || request.mockResponse.httpStatus == null || request.mockResponse.httpStatus == '') {
                    $scope.showRedMessage({data: {errors: new Array('Field <b>HTTP status code</b> is required')}});
                    return;
                }

                var requestParams = {
                    projectId: projectListService.getCurrent().id,
                    groupId: groupListService.getCurrent().id
                }
                if (request.id != null) {
                    requestParams['requestId'] = request.id;
                    requestService.ajax.update(requestParams, request, function(response) {
                        $scope.showGreenMessage('Mock-request <b>' + request.name + '</b> successfully updated');
                    }, function(errorResponse) {
                        $scope.showRedMessage(errorResponse);
                    });
                } else {
                    requestService.ajax.save(requestParams, request, function(response) {
                        requestListService.getData().push(response);
                        requestListService.setFilteredData(response);
                        requestListService.setCurrent(response);
                        $scope.showGreenMessage('Mock-request <b>' + response.name + '</b> has been created');
                    }, function(errorResponse) {
                        $scope.showRedMessage(errorResponse);
                    });
                }
            }

            $scope.isPrevRequestDisabled = function() {
                if (requestListService.getCurrent().id == null) {
                    return true;
                } else {
                    return (requestListService.filteredDataCurrentIndex <= 0);
                }
            }

            $scope.isNextRequestDisabled = function() {
                if (requestListService.getCurrent().id == null) {
                    return true;
                } else {
                    return (requestListService.filteredDataCurrentIndex >= requestListService.getFilteredData().length - 1);
                }
            }

            $scope.nextRequest = function() {
                requestListService.filteredDataCurrentIndex++;
                requestListService.setCurrent(requestListService.getFilteredData()[requestListService.filteredDataCurrentIndex])
            }

            $scope.prevRequest = function() {
                requestListService.filteredDataCurrentIndex--;
                requestListService.setCurrent(requestListService.getFilteredData()[requestListService.filteredDataCurrentIndex])
            }
}]);
