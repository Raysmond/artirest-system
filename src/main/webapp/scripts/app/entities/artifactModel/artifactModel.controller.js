'use strict';

angular.module('artirestApp')
    .controller('ArtifactModelController', function ($scope, $state, ArtifactModel, ParseLinks) {

        $scope.artifactModels = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ArtifactModel.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.artifactModels = result;
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
            $scope.artifactModel = {
                name: null,
                comment: null,
                createdAt: null,
                updatedAt: null,
                id: null
            };
        };
    });
