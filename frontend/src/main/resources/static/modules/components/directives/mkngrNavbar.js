angular.module('mockengerClientComponents')
    .directive('mkngrNavbar', ['$cookies', 'SECURITY', function($cookies, SECURITY) {
        return {
            restrict: 'E',
            templateUrl: '/modules/main/views/mkngrNavbar.html',
            link: function(scope, element) {
                if (scope.isSecurityMode()) {
                    scope.user = null;
                    if ($cookies.get('user') != null) {
                        scope.user = angular.fromJson($cookies.get('user'));
                    }
                }
            }
        };
    }]);