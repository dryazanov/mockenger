'use strict';

angular.module('mockengerClientMainApp').factory('groupService', ['$rootScope', '$resource', 'apiEndpointsService',
	function($rootScope, $resource, apiEndpointsService) {

		var GroupService = {
			ajax: $resource(apiEndpointsService.getGroupRestUrl(), {
					groupCode: '@groupCode',
					projectCode: '@projectCode'
				}, {
					update: {
						method:'PUT'
					}
				}
			),

			openGroupModal: function(project, group) {
				if (group == null) {
					group = {
						groupId: null,
						projectId: project.id,
						projectCode: project.code,
						code: null,
						name: null,
						recording: true,
						forwarding: false,
						recordTo: null
					};
				}
				$rootScope.$broadcast('openGroupModal', group);
			}
		};

		return GroupService;
}]);
