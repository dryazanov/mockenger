'use strict';
angular.module('mockengerClientComponents').factory('apiEndpointsService', ['CLIENT_VERSION', function (CLIENT_VERSION) {

    function doSomethingUseful() {
        return CLIENT_VERSION;
    }

    return {
        doSomethingUseful : doSomethingUseful
    };

}]);
