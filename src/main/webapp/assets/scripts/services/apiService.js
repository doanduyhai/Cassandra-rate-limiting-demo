'use strict';

demoModule.service('apiService', ['$http', function ($http) {

    this.setRateLimitingThreshold = function (threshold, success, error) {
        $http.put('ratelimit/threshold/' + threshold)
            .success(success).error(error);
    };

    this.rateLimitingInsertValue = function (value, ttl, success, error) {
        $http.put('ratelimit/' + value + '/' + ttl)
            .success(success).error(error);
    };

    this.fetchRateLimits = function (success, error) {
        $http.get('ratelimits')
            .success(success).error(error);
    };

    this.createSuccess = function ($scope) {
        return function (data, status) {
            $scope.success = true;
            $scope.error = false;
            $scope.httpResponse = {
                code: status,
                data: data
            };
        };
    };

    this.createError = function ($scope) {
        return function (data, status) {
            $scope.success = false;
            $scope.error = true;
            $scope.httpResponse = {
                code: status,
                data: data
            };
        };
    };
}]);
