'use strict';

angular.module('mockengerClientComponents')
    .controller('rootController', ['$rootScope', '$scope', '$http', '$location', '$cookies', 'apiEndpointsService', 'ngToast', 'ENV', 'SECURITY', 'APP_VERSION', 'BUILD_DATE',
        function ($rootScope, $scope, $http, $location, $cookies, apiEndpointsService, ngToast, ENV, SECURITY, APP_VERSION, BUILD_DATE) {

            $scope.app = {
                env: ENV,
                version: APP_VERSION,
                buildDate: BUILD_DATE
            };

            var STATUS = {
                IDLE: 0,
                LOADING: 1,
                ERROR: 2
            };

            $scope.setContentLoading = function () {
                return STATUS.LOADING;
            };

            $scope.setContentReady = function () {
                return STATUS.IDLE;
            };

            $scope.setContentLoadingFailed = function () {
                return STATUS.ERROR;
            };

            $scope.isContentLoading = function (status) {
                return status === STATUS.LOADING;
            };

            $scope.isContentReady = function (status) {
                return status === STATUS.IDLE;
            };

            $scope.isContentLoadingFailed = function (status) {
                return status === STATUS.ERROR;
            };

            $scope.signOut = function() {
                $location.path('/logout');
            }

            $scope.isAdmin = function(role) {
                if (SECURITY) {
                    return (role == 'ADMIN' ? true : false);
                }
                return true;
            }

            $scope.isManagerOrAdmin = function(role) {
                if (SECURITY) {
                    return (role == 'MANAGER' || $scope.isAdmin(role) ? true : false);
                }
                return true;
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
                        for (var i = 0, l = error.data.errors.length; i < l; i++) {
                            ngToast.create({
                                className: 'danger',
                                dismissOnTimeout: false,
                                content: error.data.errors[i]
                            });
                        }
                    } else if (error.data.error != null) {
                        var errorMessage = null;
                        if (error.data.error_description != null) {
                            var err = error.data.error;
                            var msg = error.data.error_description;
                            errorMessage = msg;

                            if (SECURITY) {
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
        }
]);
