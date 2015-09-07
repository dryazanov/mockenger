'use strict';

angular.module('mockengerClientMainApp')
    .controller('RequestListController', [
        '$scope',
        '$filter',
        'ngToast',
        'projectListService',
        'groupListService',
        'requestService',
        'requestListService',
        'REQUESTS_PER_PAGE',

        function($scope, $filter, ngToast, projectListService, groupListService, requestService, requestListService, REQUESTS_PER_PAGE) {
//            $scope.searchQuery = requestListService.getSearchQuery();
//            $scope.listOrder = requestListService.getListOrder();

            requestListService.setLimit(REQUESTS_PER_PAGE);

            $scope.filteredData = {};

            $scope.createRequest = function() {
                requestListService.setCurrent({
                    id: null,
                    groupId: groupListService.getCurrent().id,
                    name: null,
                    method: null,
                    path: {
                        transformers: null,
                        value: null
                    },
                    headers: {
                        transformers: null,
                        values: []
                    },
                    parameters: {
                        transformers: null,
                        values: []
                    },
                    body: {
                        transformers: null,
                        value: null
                    },
                    mockResponse: null
                });
            }

            $scope.deleteRequest = function(requestToDelete) {
                var index = requestListService.getRequestIndex(requestToDelete.id);
                if (index < 0) {
                    ngToast.create({
                        className: 'danger',
                        dismissOnTimeout: false,
                        content: 'Delete process aborted. Cannot find request in the list.'
                    });
                } else {
                    var name = requestToDelete.name;
                    var requestParams = {
                        projectId: projectListService.getCurrent().id,
                        groupId: groupListService.getCurrent().id,
                        requestId: requestToDelete.id
                    };
                    requestService.ajax.delete(requestParams, function(response, getResponseHeaders) {
                        requestListService.removeFromRequestList(index);
                        ngToast.create('Mock-request <b>' + name + '</b> has been deleted');

                        if ((requestListService.getCurrentPage() + 1) > $scope.pageCount()) {
                            $scope.prevPage();
                        }
                    }, function(errorResponse) {
                        showErrors(errorResponse);
                    });
                }
            };

            $scope.$watchGroup([requestListService.getListOrder, requestListService.getSearchQuery], function() {
                requestListService.setCurrentPage(0);
            });

            $scope.selectRequest = function(index, request) {
                requestListService.filteredDataCurrentIndex = $scope.getOffset() + index;
                requestListService.setFilteredData(requestListService.getData());
                if (requestListService.filters.search.query != undefined && requestListService.filters.search.query != '') {
                    requestListService.setFilteredData($filter('filter')(requestListService.getFilteredData(), requestListService.filters.search.query));
                }
                requestListService.setFilteredData($filter('orderBy')(requestListService.getFilteredData(), requestListService.filters.order));
                requestListService.setCurrent(request);
            }

            $scope.range = function() {
                var range = [];
                for (var i = 0, l = $scope.pageCount(); i < l; i++) {
                    range.push(i);
                }
                return range;
            };

            $scope.prevPage = function() {
                if (requestListService.getCurrentPage() > 0) {
                    requestListService.setCurrentPage(requestListService.getCurrentPage() - 1);
                }
            };

            $scope.nextPage = function() {
                if (requestListService.getCurrentPage() < $scope.pageCount() - 1) {
                    requestListService.setCurrentPage(requestListService.getCurrentPage() + 1);
                }
            };

            $scope.isActive = function(n) {
                return (requestListService.getCurrentPage() == n);
            };

            $scope.isPrevPageDisabled = function() {
                return (requestListService.getCurrentPage() === 0);
            };

            $scope.isNextPageDisabled = function() {
                return (requestListService.getCurrentPage() === $scope.pageCount() - 1);
            };

            $scope.getOffset = function() {
                return (requestListService.getCurrentPage() * requestListService.getLimit());
            }

            $scope.pageCount = function() {
                if (requestListService.getData() != null) {
                    var filtered = $filter('filter')(requestListService.getData(), requestListService.getSearchQuery());
                    return Math.ceil(filtered.length / requestListService.getLimit());
                }
                return 0;
            };
}]);