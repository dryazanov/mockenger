'use strict';

angular.module('mockengerClientMainApp')
	.controller('RequestController',[
		'$scope',
		'$confirm',
		'$timeout',
		'projectListService',
		'groupListService',
		'requestService',
		'requestListService',

		function ($scope, $confirm, $timeout, projectListService, groupListService, requestService, requestListService) {
			var REGEXP = 'REGEXP';
			var KEY_VALUE = 'KEY_VALUE';
			var XPATH = 'XPATH';
			var APPLICATION_FORM_URLENCODED_VALUE = 'application/x-www-form-urlencoded';

			$scope.bodyTransformerTypes = new Array(REGEXP, XPATH);

			$scope.isXPathTransformer = function(transformer) {
				return transformer.type === XPATH;
			}

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

			$scope.selectMethod = function(method) {
				requestListService.getCurrent().method = method;
			}

			$scope.selectTransformerType = function(type, transformer) {
				transformer.type = type;
			}

			$scope.addParameter = function() {
				if ($scope.getParams() == null) {
					requestListService.getCurrent().parameters.values = [];
				}

				pushKeyValuePair($scope.getParams());
			}

			$scope.addRequestHeader = function() {
				if ($scope.getRequestHeaders() == null) {
					requestListService.getCurrent().headers.values = [];
				}

				pushKeyValuePair($scope.getRequestHeaders());
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
				$scope.deleteElement(index, source, 'header');
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

			// Add namespace to XPath transformer
			$scope.addXPathNamespace = function(transformer) {
				if (transformer.namespaces == null) {
					transformer.namespaces = [];
				}

				transformer.namespaces.push({
					key: null,
					value: null
				});
			}

			$scope.deleteElement = function(index, source, elementName) {
				$confirm({
					text: "Do you really want to delete this " + elementName + "?"
				}).then(function() {
					if (source != null && source[index] != null) {
						source.splice(index, 1);
					}
				}, function() {
					// cancel
				});
			}

			$scope.deleteTransformer = function(index, source) {
				$scope.deleteElement(index, source, 'transformer');
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

				request.latency = $scope.cleanUpLatency(requestListService.getCurrent().latencyType, request.latency);

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
				$scope.setCurrentRequest();
			}

			$scope.prevRequest = function() {
				requestListService.filteredDataCurrentIndex--;
				$scope.setCurrentRequest();
			}

			$scope.setCurrentRequest = function(index) {
				var request = requestListService.getFilteredData()[requestListService.filteredDataCurrentIndex]

				request.latencyType = $scope.getLatencyType(request.latency);
				requestListService.setCurrent(request);
			}

			$scope.isRequestTabDisabled = function() {
				return ($scope.getMethod() != null && isMethodWithBody($scope.getMethod()) ? false : true);
			}

			$scope.getMethod = function() {
				return requestListService.getCurrent().method;
			}

			$scope.getParams = function() {
				return requestListService.getCurrent().parameters.values;
			}

			$scope.getRequestHeaders = function() {
				return requestListService.getCurrent().headers.values;
			}

			$scope.hasLatencyOnGroupLevel = function() {
				var group = groupListService.getCurrent() || null;

				if (group != null && group.latency != undefined && group.latency != null) {
					groupListService.getCurrent().latencyType = $scope.getLatencyType(groupListService.getCurrent().latency);

					var latency = $scope.cleanUpLatency(group.latencyType, group.latency);

					if (latency != null && latency.fixed > 0 || (latency.min > 0 && latency.max > 0)) {
						return true;
					}
				}

				return false;
			}

			$scope.isLatencyTypeNotNull = function() {
				return (requestListService.getCurrent().latencyType != null);
			}


			// ================
			// Clipboard
			// ================
			$scope.clipboard = null;
			$scope.clipboardMessage = {};

			$scope.clipboardInit = function() {
				$scope.clipboard = new window.Clipboard('.curlCommand');

				$scope.clipboard.on('success', function(e) {
					$scope.clipboardMessage = {
						failure : false,
						text: 'Copied'
					};

					$timeout(function() {
						$scope.clipboardMessage = {};
					}, 3000);

					e.clearSelection();
				});

				$scope.clipboard.on('error', function(e) {
					$scope.clipboardMessage = {
						failure : true,
						text: 'Can\'t copy to clipboard'
					};

					$timeout(function() {
						$scope.clipboardMessage = {};
					}, 3000);
				});
			}

			$scope.clipboardInit();


			// ================
			// cURL
			// ================
			$scope.getCURL = function() {
				return "curl -i -X " + requestListService.getCurrent().method + " \\\n " +
					(
						'\'' +
						groupListService.getUrlForNewRequests() +
						requestListService.getCurrent().path.value +
						$scope.getParametersAsString() +
						'\''
					)
					+ $scope.getHeadersAsString()
					+ (!$scope.isRequestTabDisabled() ? " \\\n -d '" + $scope.getBody() + "'" : '')
			}

			$scope.getBody = function() {
				if (requestListService.getCurrent().body != null && requestListService.getCurrent().body.value != null) {
					return requestListService.getCurrent().body.value;
				}

				return '';
			}

			$scope.getHeadersAsString = function() {
				var result = "";
				var headers = $scope.getRequestHeaders();

				for (var i = 0, l = headers.length; i < l; i++) {
					result += " \\\n -H '" + headers[i].key + ": " + headers[i].value + "'";
				}

				return result;
			}

			$scope.getParametersAsString = function() {
				var result = "";
				var parameters = $scope.getParams();

				for (var i = 0, l = parameters.length; i < l; i++) {
					result += (result.length == 0 ? "?" : "&") + parameters[i].key + "=" + window.encodeURIComponent(parameters[i].value);
				}

				return result;
			}

			$scope.isURLEncodedType = function() {
				if (!$scope.isRequestTabDisabled()) {
					var headers = $scope.getRequestHeaders();

					for (var i = 0, l = headers.length; i < l; i++) {
						if (headers[i].key.trim().toLowerCase() === 'content-type'
							&& headers[i].value.trim().toLowerCase() === APPLICATION_FORM_URLENCODED_VALUE) {

							return true;
						}
					}
				}

				return false;
			}


			// ================
			// PRETTY PRINT
			// ================
			$scope.prettyModel = {
				Request: {
					xml : false,
					json: false
				},
				Response: {
					xml : false,
					json: false
				}
			}

			$scope.prettify = function(model, type) {
				$scope.prettyModel[model].xml = false;
				$scope.prettyModel[model].json = false;
				$scope.prettyModel[model][type] = true;

				$scope['cmMode' + model] = (type === 'json' ? 'javascript' : type);
				$scope['modeChanged' + model]();

				try {
					if (model === 'Request') {
						requestListService.getCurrent().body.value = $.format(requestListService.getCurrent().body.value, {method: type});
					} else if (model === 'Response') {
						requestListService.getCurrent().mockResponse.body = $.format(requestListService.getCurrent().mockResponse.body, {method: type});
					}
				} catch (err) {
					// do nothing
				}
			}


			// ================
			// CODE MIRROR
			// ================
			$scope.updateCmEditor = function(type) {
				$scope['cmRefresh' + type] = true;
				$scope['cmEditor' + type].refresh();
			}

			$scope.onRequestTabClick = function() {
				setTimeout(function() {
					$scope.updateCmEditor('Request');
				}, 100);
			};

			$scope.onResponseTabClick = function() {
				setTimeout(function() {
					$scope.updateCmEditor('Response');
				}, 100);
			};

			$scope.getCodeMirrorOptions = function(type) {
				return {
					lineNumbers: true,
					indentWithTabs: false,
					lineWrapping: true,
					autoRefresh: true,

					onLoad: function(_editor) {
						$scope['cmRefresh' + type] = false;
						$scope['cmEditor' + type] = _editor;
						$scope['modeChanged' + type] = function() {
							_editor.setOption("mode", $scope['cmMode' + type].toLowerCase());
						};
					}
				};
			}

			$scope.cmEditorRequest = null;
			$scope.cmEditorResponse = null;
			$scope.cmModeRequest = 'scheme';
			$scope.cmModeResponse = 'scheme';
			$scope.cmOptionRequest = $scope.getCodeMirrorOptions('Request');
			$scope.cmOptionResponse = $scope.getCodeMirrorOptions('Response');
}]);
