'use strict';

angular.module('mockengerClientMainApp').factory('testService', [function () {

    function doSomethingUseful() {
        return 'it works';
    }

    return {
        doSomethingUseful : doSomethingUseful
    };

}]);
