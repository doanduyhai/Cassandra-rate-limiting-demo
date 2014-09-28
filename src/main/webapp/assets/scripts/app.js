'use strict';

var demoModule = angular.module('demoApp', ['ngRoute','ui.include','ui.bootstrap','ui.slider'])
    .config(function ($routeProvider, $httpProvider) {
        $httpProvider.defaults.headers.common = {'Accept': 'application/json', 'Content-Type': 'application/json'};

        $routeProvider
            .when('/', {
                templateUrl: 'assets/views/main.html'
            })
	        .when('/ratelimit', {
	        	templateUrl: 'assets/views/ratelimiting.html'
	        });
});

