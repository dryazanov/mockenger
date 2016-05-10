'use strict';

angular.module('mockengerClientMainApp')
    .factory('requestListService', ['$resource', 'apiEndpointsService',
        function ($resource, apiEndpointsService) {

            var RequestListService = {
                data: null,

                filteredData: null,

                filteredDataCurrentIndex: 0,

                current: null,

                defaultOrderField: 'creationDate',

                filters: {
                    order: this.defaultOrderField,
                    search: {
                        query: undefined
                    },
                    paging: {
                        limit: 10,
                        current: 0
                    }
                },

                getListOrder: function() {
                    return RequestListService.filters.order;
                },

                setListOrder: function(field) {
                    RequestListService.filters.order = field;
                },

                getSearchQuery: function() {
                    return RequestListService.filters.search.query;
                },

                setSearchQuery: function(query) {
                    RequestListService.filters.search.query = query;
                },

                getLimit: function() {
                    return RequestListService.filters.paging.limit;
                },

                setLimit: function(limit) {
                    return RequestListService.filters.paging.limit = limit;
                },

                getCurrentPage: function() {
                    return RequestListService.filters.paging.current;
                },

                setCurrentPage: function(n) {
                    RequestListService.filters.paging.current = n;
                },

                getCurrent: function() {
                    return RequestListService.current;
                },

                setCurrent: function(currentToSet) {
                    RequestListService.current = currentToSet;
                },

                setData: function(data) {
                    RequestListService.data = data;
                    RequestListService.setCurrentPage(0);
                    RequestListService.setListOrder(RequestListService.defaultOrderField);
                    RequestListService.setSearchQuery(undefined);
                },

                getData: function() {
                    return RequestListService.data;
                },

                setFilteredData: function(data) {
                    RequestListService.filteredData = data;
                },

                getFilteredData: function() {
                    return RequestListService.filteredData;
                },

                ajax: $resource(apiEndpointsService.getRequestRestUrl(), {
                        groupId: '@groupId',
                        projectId: '@projectId'
                    }, {}
                ),

                getRequestIndex: function(id) {
                    if (RequestListService.getData() != null) {
                        for (var index = 0, l = RequestListService.getData().length; index < l; index++) {
                            if (id == RequestListService.getData()[index].id) {
                                return index;
                            }
                        }
                    }
                    return -1;
                },

                removeFromRequestList: function(index) {
                    RequestListService.data.splice(index, 1);
                }
            };

            return RequestListService;
}]);
