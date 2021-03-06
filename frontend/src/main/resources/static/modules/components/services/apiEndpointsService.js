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
			},
			roles: function() {
				return API_BASE_PATH + '/valueset/roles';
			},
			eventTypes: function() {
				return API_BASE_PATH + '/valueset/eventTypes';
			},
			eventEntityTypes: function() {
				return API_BASE_PATH + '/valueset/eventEntityTypes';
			}
		},
		getEventRestUrl: function() {
			return API_BASE_PATH + '/events/:eventId';
		},
		getProjectRestUrl: function() {
			return API_BASE_PATH + '/projects/:projectCode';
		},
		getAccountRestUrl: function() {
			return API_BASE_PATH + '/accounts/:accountId';
		},
		getGroupRestUrl: function() {
			return restUrls.getProjectRestUrl() + '/groups/:groupCode';
		},
		getRequestRestUrl: function() {
			return restUrls.getGroupRestUrl() + '/requests/:requestCode';
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
