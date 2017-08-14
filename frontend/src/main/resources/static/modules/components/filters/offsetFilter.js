'use strict';

angular.module('mockengerClientComponents').filter('offset', function() {
    return function(input, start) {
        start = parseInt(start, 10);
        return input.slice(start);
    };
});