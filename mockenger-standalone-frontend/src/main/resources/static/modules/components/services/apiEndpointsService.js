'use strict';
angular.module('mockengerClientComponents').factory('apiEndpointsService', ['API_BASE_PATH', function (API_BASE_PATH) {

    function getProjectRestUrl() {
        return API_BASE_PATH + '/projects/:projectId';
    }

    function getGroupRestUrl() {
        return getProjectRestUrl() + '/groups/:groupId';
    }
    return {
        getProjectRestUrl : getProjectRestUrl,
        getGroupRestUrl: getGroupRestUrl

    };

}]);
