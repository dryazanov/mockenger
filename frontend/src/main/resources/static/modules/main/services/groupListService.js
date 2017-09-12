'use strict';

angular.module('mockengerClientMainApp')
	.factory('groupListService', ['$resource', 'apiEndpointsService',
		function ($resource, apiEndpointsService) {

			var GroupListService = {
				data: null,

				current: null,

				urlForNewRequests: null,


				getData: function() {
					return GroupListService.data;
				},

				setData: function(dataToSet) {
					GroupListService.data = dataToSet;
				},

				getCurrent: function() {
					return GroupListService.current;
				},

				setCurrent: function(currentToSet) {
					GroupListService.current = currentToSet;
				},

				ajax: $resource(apiEndpointsService.getGroupRestUrl(), {groupCode: '@groupCode'}, {}),

				addGroupToList: function(group) {
					if (GroupListService.data == null) {
						GroupListService.data = [];
					}
					GroupListService.data.push(group);
				},

				removeFromGroupList: function(index) {
					GroupListService.data.splice(index, 1);
				},

				setUrlForNewRequests: function(url) {
					GroupListService.urlForNewRequests = url;
				},

				getUrlForNewRequests: function() {
					return GroupListService.urlForNewRequests;
				}
			};

			return GroupListService;
}]);
