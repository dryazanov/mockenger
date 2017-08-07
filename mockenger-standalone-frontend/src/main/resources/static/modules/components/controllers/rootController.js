'use strict';

angular.module('mockengerClientComponents')
    .controller('rootController', ['$rootScope', '$scope', '$http', '$location', 'ngToast', 'ngProgressFactory', 'ENV', 'SECURITY', 'APP_VERSION', 'BUILD_DATE',
        function ($rootScope, $scope, $http, $location, ngToast, ngProgressFactory, ENV, SECURITY, APP_VERSION, BUILD_DATE) {

            $scope.app = {
                env: ENV,
                version: APP_VERSION,
                buildDate: BUILD_DATE
            };

            $scope.signOut = function() {
                $location.path('/logout');
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
        }
]);
