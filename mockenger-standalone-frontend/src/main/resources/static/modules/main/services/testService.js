'use strict';

angular.module('mockengerClientMainApp').factory('testService', ['CLIENT_VERSION', function (CLIENT_VERSION) {

    function doSomethingUseful() {
        return CLIENT_VERSION;
    }

    return {
        doSomethingUseful : doSomethingUseful
    };

}]);
