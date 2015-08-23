'use strict';

module.factory('requestsService', ['$q', '$filter', '$resource', 'apiEndpointsService', function ($q, $filter, $resource, apiEndpointsService) {
    var RequestService = {
        selectedRequest: null,

        requestsList: {
            data: [],
            requestQuery: undefined,
            orderProp: 'name',
            paginator: {
                itemsPerPage: 10,
                currentPage: 0
            }
        },

        ajax: $resource(apiEndpointsService.getRequestRestUrl(),
            {
                groupId: '@groupId',
                projectId: '@projectId',
                requestId: '@requestId'
            }, {}
        ),

        refreshData: function(response) {
            var deferred = $q.defer();

            this.setData(response);
            this.setCurrentPage(0);
            this.selectedRequest = null;

            deferred.resolve(this.requestsList);
            return deferred.promise;
        },
        setData: function(data) {
            this.requestsList.data = data;
        },
        getData: function() {
            return this.requestsList.data;
        },
        getCurrentPage: function() {
            return this.requestsList.paginator.currentPage;
        },
        setCurrentPage: function(n) {
            this.requestsList.paginator.currentPage = n;
        }
    };

    return RequestService;
}]);
