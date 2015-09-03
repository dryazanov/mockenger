'use strict';

angular.module('mockengerClientMainApp').factory('requestListService', ['$resource', 'apiEndpointsService', function ($resource, apiEndpointsService) {

    var RequestService = {
        data: null,
        current: null,
        filters: {
            order: 'name',
            search: {
                query: undefined
            },
            paging: {
                limit: 10,
                current: 0
            }
        },

        getListOrder: function() {
            return RequestService.filters.order;
        },

        setListOrder: function(field) {
            RequestService.filters.order = field;
        },

        getSearchQuery: function() {
            return RequestService.filters.search.query;
        },

        setSearchQuery: function(query) {
            RequestService.filters.search.query = query;
        },

        getLimit: function() {
            return RequestService.filters.paging.limit;
        },

        setLimit: function(limit) {
            return RequestService.filters.paging.limit = limit;
        },

        getCurrentPage: function() {
            return RequestService.filters.paging.current;
        },

        setCurrentPage: function(n) {
            RequestService.filters.paging.current = n;
        },

        getCurrent: function() {
            return RequestService.current;
        },

        setCurrent: function(currentToSet) {
            RequestService.current = currentToSet;
        },

        setData: function(data) {
            RequestService.data = data;
            RequestService.setCurrentPage(0);
            RequestService.setListOrder('name');
            RequestService.setSearchQuery(undefined);
        },

        getData: function() {
            return RequestService.data;
        },

        ajax: $resource(apiEndpointsService.getRequestRestUrl(), {
                groupId: '@groupId',
                projectId: '@projectId',
                requestId: '@requestId'
            }, {}
        )
    };

    return RequestService;
}]);
