'use strict';

angular.module('mockengerClientMainApp')
    .controller('RequestController',[
        '$scope',
        'ngToast',
        'projectListService',
        'groupListService',
        'requestService',
        'requestListService',
        'valuesetService',

        function ($scope, ngToast, projectListService, groupListService, requestService, requestListService, valuesetService) {
            var pushKeyValuePair = function(source) {
                source.push({
                    key: "",
                    value: ""
                });
            }

            var addTransformer = function(source) {
                source.push({
                    type: 'REGEXP',
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

            $scope.deleteTransformer = function(index, source) {
                if (source != null && source[index] != null) {
                    source.splice(index, 1);
                }
            }

            $scope.saveRequest = function(currentRequest) {
                if (currentRequest.name == null || currentRequest.name == '') {
                    ngToast.create({
                        className: 'danger',
                        dismissOnTimeout: false,
                        content: 'Field <b>Name</b> is required'
                    });
                    return;
                }
                if (currentRequest.mockResponse == null || currentRequest.mockResponse.httpStatus == null || currentRequest.mockResponse.httpStatus == '') {
                    ngToast.create({
                        className: 'danger',
                        dismissOnTimeout: false,
                        content: 'Field <b>HTTP status code</b> is required'
                    });
                    return;
                }

                var requestParams = {
                    projectId: projectListService.getCurrent().id,
                    groupId: groupListService.getCurrent().id
                }
                if (currentRequest.id != null) {
                    requestParams.requestId = currentRequest.id;
                    requestService.ajax.update(requestParams, currentRequest, function(response) {
                        ngToast.create('Mock-request <b>' + currentRequest.name + '</b> has been saved');
                    }, function(errorResponse) {
                        ngToast.create({
                            className: 'danger',
                            dismissOnTimeout: false,
                            content: errorResponse.data.errors[0]
                        });
                    });
                } else {
                    requestService.ajax.save(requestParams, currentRequest, function() {
                        requestListService.getData().push(currentRequest);
                        ngToast.create('New mock-request has been created');
                    }, function(errorResponse) {
                        ngToast.create({
                            className: 'danger',
                            dismissOnTimeout: false,
                            content: errorResponse.data.errors[0]
                        });
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
