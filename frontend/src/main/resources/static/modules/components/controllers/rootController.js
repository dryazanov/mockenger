'use strict';

angular.module('mockengerClientComponents')
	.controller('rootController', [
		'$rootScope',
		'$scope',
		'$http',
		'$location',
		'$cookies',
		'ngToast',
		'ngProgressFactory',
		'ENV',
		'SECURITY',
		'APP_VERSION',
		'BUILD_DATE',

		function ($rootScope, $scope, $http, $location, $cookies, ngToast, ngProgressFactory, ENV, SECURITY, APP_VERSION, BUILD_DATE) {

			$scope.app = {
				env: ENV,
				version: APP_VERSION,
				buildDate: BUILD_DATE
			};

			$scope.signOut = function() {
				$location.path('/logout');
			}

			$scope.changeTheme = function() {
				var darkTheme = $('#dark-theme');

				if (darkTheme.attr('rel') === 'stylesheet alternate') {
					darkTheme.attr('rel', 'stylesheet');

					var expireDate = new Date();
					expireDate.setDate(expireDate.getYear() + 10);

					$cookies.put('ui-theme', 'dark', {'expires': expireDate});
				} else {
					darkTheme.attr('rel', 'stylesheet alternate');
					$cookies.remove('ui-theme');
				}
			}

			$scope.isSecurityMode = function() {
				return (String(SECURITY) === 'true');
			}

			$scope.isAdmin = function(role) {
				return ($scope.isSecurityMode() ? role === 'ADMIN' : true);
			}

			$scope.isManagerOrAdmin = function(role) {
				return ($scope.isSecurityMode() ? (role === 'MANAGER' || $scope.isAdmin(role)) : true);
			}

			$scope.getErrorStyle = function(input) {
				return ($scope.hasError(input) ? 'has-error' : '');
			}

			$scope.hasError = function(input) {
				return (input.$invalid && !input.$pristine);
			}

			$scope.showGreenMessage = function(text) {
				ngToast.create(text);
			}

			$scope.isActive = function(location) {
				return ($location.path().startsWith(location));
			};

			$scope.showRedMessage = function(error) {
				if (error != null && error.data != null) {
					if (error.data.errors != null && error.data.errors.length > 0) {
						$scope.showErrorsFromList(error.data.errors);
					} else if (error.data.error != null) {
						var errorMessage = null;
						if (error.data.error_description != null) {
							var err = error.data.error;
							var msg = error.data.error_description;
							errorMessage = msg;

							if ($scope.isSecurityMode()) {
								if (err == 'unauthorized' || (err == 'invalid_token' && msg.indexOf('Invalid refresh token (expired)') >= 0)) {
									errorMessage = "Authorization required";
									$location.path('/login');
								} else if (err == 'invalid_grant' && msg.indexOf('Bad credentials') >= 0) {
									errorMessage = "The username or password is incorrect";
								}
							}
						} else {
							errorMessage = error.data.error;
						}
						if (errorMessage != null) {
							ngToast.create({
								className: 'danger',
								dismissOnTimeout: false,
								content: errorMessage
							});
						}
					}
				}
			}

			$scope.showErrorsFromList = function(errors) {
				for (var i = 0, l = errors.length; i < l; i++) {
					ngToast.create({
						className: 'danger',
						dismissOnTimeout: false,
						content: errors[i]
					});
				}
			}


			$scope.cleanUpLatency = function(latencyType, latency) {
				if (latencyType == null) {
					return latencyType;
				}

				if (latencyType) {
					var fixed = parseInt(latency.fixed);

					if (fixed > 0) {
						latency.fixed = fixed;
					} else {
						latency.fixed = 0;
					}

					latency.min = 0;
					latency.max = 0;
				} else {
					var min = parseInt(latency.min);
					var max = parseInt(latency.max);

					if (min > 0 && max > 0) {
						latency.min = min;
						latency.max = max;
					} else {
						latency.min = 0;
						latency.max = 0;
					}

					latency.fixed = 0;
				}

				return latency;
			}

			$scope.getLatencyType = function(latency) {
				if (latency == null) {
					return latency;
				}

				return !(latency.min > 0 && latency.max > 0);
			}
		}
]);
