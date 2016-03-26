'use strict';

angular.module('artirestApp')
    .controller('LogController', function ($scope, $state, Log, ParseLinks) {

        $scope.logs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Log.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.logs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.log = {
                title: null,
                processId: null,
                artifactId: null,
                fromState: null,
                toState: null,
                service: null,
                memo: null,
                createdAt: null,
                type: null,
                id: null
            };
        };
    });
