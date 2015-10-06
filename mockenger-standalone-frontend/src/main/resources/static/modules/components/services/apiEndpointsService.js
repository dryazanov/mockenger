'use strict';

angular.module('mockengerClientComponents').factory('apiEndpointsService', ['API_BASE_PATH', function(API_BASE_PATH) {
    var restUrls = {
        getValuesetRestUrl: {
            projectTypes: function() {
                return API_BASE_PATH + '/valueset/projectTypes';
            },
            requestMethods: function() {
                return API_BASE_PATH + '/valueset/requestMethods';
            },
            transformerTypes: function() {
                return API_BASE_PATH + '/valueset/transformerTypes';
            },
            headers: function() {
                return API_BASE_PATH + '/valueset/headers';
            }
        },
        getProjectRestUrl: function() {
            return API_BASE_PATH + '/projects/:projectId';
        },
        getGroupRestUrl: function() {
            return restUrls.getProjectRestUrl() + '/groups/:groupId';
        },
        getRequestRestUrl: function() {
            return restUrls.getGroupRestUrl() + '/requests/:requestId';
        },
        getOAuth2AccessTokenUrl: function() {
            return API_BASE_PATH + '/oauth/token?grant_type=password';
        },
        getOAuth2RefreshTokenUrl: function() {
            return API_BASE_PATH + '/oauth/token?grant_type=refresh_token';
        },
        getOAuth2LogoutUrl: function() {
            return API_BASE_PATH + '/oauth/revoke';
        },
        getUserData: function() {
            return API_BASE_PATH + '/oauth/user';
        }
    };

    return restUrls;
}]);
