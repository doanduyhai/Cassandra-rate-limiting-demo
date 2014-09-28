demoModule.controller('RateLimitingCtrl', [ '$scope', '$timeout', 'apiService', function ($scope, $timeout, $apiService) {
    var pollingPromise;
    var pattern = new RegExp("value([0-9]+)");

    $scope.success = false;
    $scope.error = false;
    $scope.ratelimiting = {
        insertionDisabled: true,
        openConfig: true,
        openData: false,
        threshold: 5,
        ttl: 10,
        value: "value1"
    };

    $scope.setThreshold = function () {
        $apiService.setRateLimitingThreshold($scope.ratelimiting.threshold,
            function (data, status) {
                $scope.ratelimiting.insertionDisabled = false;
                $scope.ratelimiting.openConfig = false;
                $scope.ratelimiting.openData = true;
                $apiService.createSuccess($scope)(data, status);
            },
            function (data, status) {
                $scope.ratelimiting.insertionDisabled = true;
                $scope.ratelimiting.openConfig = true;
                $scope.ratelimiting.openData = true;
                $apiService.createError($scope)(data, status);
            })
    };

    $scope.insertValueAndPoll = function () {
        $apiService.rateLimitingInsertValue($scope.ratelimiting.value, $scope.ratelimiting.ttl,
            function (data, status) {
                cleanPolling();
                pollingPromise = $timeout(fetchRateLimits, 10);
                incrementInputValue();
                $apiService.createSuccess($scope)(data, status);
            },
            function (data, status) {
                cleanPolling();
                $apiService.createError($scope)(data, status);
            }
        );
    };


    function fetchRateLimits() {
        $apiService.fetchRateLimits($apiService.createSuccess($scope), $apiService.createError($scope));
        pollingPromise = $timeout(fetchRateLimits, 1000);
    }

    function cleanPolling() {
        if (pollingPromise) {
            $timeout.cancel(pollingPromise);
        }
    }

    function incrementInputValue() {
        if (pattern.test($scope.ratelimiting.value)) {
            var nextVal = Math.floor(pattern.exec($scope.ratelimiting.value)[1]) + 1;
            console.log(" nextVal = " + nextVal);
            console.log(" $scope.ratelimiting.value = " + $scope.ratelimiting.value);
            $scope.ratelimiting.value = "value" + nextVal;
        }
    }

    $scope.stopPolling = function () {
        cleanPolling();
        delete $scope.httpResponse;
        delete $scope.success;
        delete $scope.error;
    };

} ]);
