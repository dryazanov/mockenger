'use strict';

angular.module('mockengerClientMainApp')
    .controller('RequestController',[
        '$scope',
        '$confirm',
        'projectListService',
        'groupListService',
        'requestService',
        'requestListService',

        function ($scope, $confirm, projectListService, groupListService, requestService, requestListService) {
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

            var isMethodWithBody = function(method) {
                return (method === 'POST' || method === 'PUT' || method === 'PATCH');
            }

            $scope.cmOptions = {
            	lineNumbers: false,
            	lineWrapping : true,
            	mode: 'javascript'
            }

//            $scope.isSomething = true;

            $scope.codemirrorLoaded = function(_editor){
                // Editor part
                var _doc = _editor.getDoc();
                _editor.focus();

                // Options
                _editor.setOption('lineNumbers', false);
                //_editor.setOption('firstLineNumber', 10);
                _editor.setOption('lineWrapping', true);
                _editor.setOption('mode', 'xml');
                _editor.setOption('autoRefresh', true);

                // Load without click
				setTimeout(function() {
					console.log('refresh');
					_editor.refresh();
				}, 2000);

                //_doc.markClean();

                // Events
//                _editor.on("beforeChange", function(){ ... });
//                _editor.on("change", function(){ ... });
              };

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
                $confirm({
                    text: "Do you really want to delete this parameter?"
                }).then(function() {
                    var source = requestListService.getCurrent().parameters.values;
                    if (source != null && source[index] != null) {
                        source.splice(index, 1);
                    }
                }, function() {
					// cancel
				});
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
                $confirm({
                    text: "Do you really want to delete this header?"
                }).then(function() {
                    if (source != null && source[index] != null) {
                        source.splice(index, 1);
                    }
                }, function() {
                	// cancel
                });
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
                $confirm({
                    text: "Do you really want to delete this transformer?"
                }).then(function() {
                    if (source != null && source[index] != null) {
                        source.splice(index, 1);
                    }
                }, function() {
					// cancel
				});
            }

            var isNameOk = function(name) {
                if (!name) {
                    $scope.showRedMessage({data: {errors: new Array('Field <b>Name</b> is required')}});
                    return false;
                }
                return true;
            }

            var isResponseDataOk = function(mockResponse) {
                if (!mockResponse || !mockResponse.httpStatus) {
                    $scope.showRedMessage({data: {errors: new Array('Field <b>HTTP status code</b> is required')}});
                    return false;
                }
                return true;
            }

            var isHttpMethodOk = function(method) {
                if (!method) {
                    $scope.showRedMessage({data: {errors: new Array('<b>Method</b> may not be null or empty')}});
                    return false;
                }
                return true;
            }

            $scope.saveRequest = function(request) {
                if (!isNameOk(request.name) || !isResponseDataOk(request.mockResponse) || !isHttpMethodOk(request.method)) {
                    return;
                }
                if (!isMethodWithBody(request.method)) {
                    request.body = null;
                }

                var requestParams = {
                    projectCode: projectListService.getCurrent().code,
                    groupCode: groupListService.getCurrent().code
                }

                if (request.id != null) {
                    requestParams.requestCode = request.code;

                    requestService.ajax.update(requestParams, request, function() {
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
                requestListService.setCurrent(requestListService.getFilteredData()[requestListService.filteredDataCurrentIndex]);
            }

            $scope.isRequestTabDisabled = function() {
                return (requestListService.getCurrent().method != null && isMethodWithBody(requestListService.getCurrent().method) ? false : true);
            }

            $scope.getCURL = function() {
                return "curl -i -X " + requestListService.getCurrent().method + " "
                    + "'" +
                    	(groupListService.getUrlForNewRequests()
                    	+ requestListService.getCurrent().path.value
                    	+ $scope.getParametersAsString())
					+ "'"
                    + " "
                    + $scope.getHeadersAsString()
                    + (!$scope.isRequestTabDisabled() ? " -d '" + $scope.getBody() + "'" : '')
            }

            $scope.getBody = function() {
            	var body = requestListService.getCurrent().body.value;

            	try {
            		return angular.toJson(angular.fromJson(body), true);
            	} catch (err) {
            		return body;
            	}
            }

            $scope.getHeadersAsString = function() {
                var result = "";
                var headers = requestListService.getCurrent().headers.values;

                for (var i = 0, l = headers.length; i < l; i++) {
                    result += " -H '" + headers[i].key + ": " + headers[i].value + "' ";
                }

                return result;
            }

            $scope.getParametersAsString = function() {
                var result = "";
                var parameters = requestListService.getCurrent().parameters.values;

                for (var i = 0, l = parameters.length; i < l; i++) {
                    result += (result.length == 0 ? "?" : "&") + parameters[i].key + "=" + parameters[i].value;
                }

                return result;
            }
}]);
