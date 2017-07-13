'use strict';

angular.module('artirestApp')
    .controller('ProcessModelController', function ($scope,$http, $state, ProcessModel, ParseLinks) {

        $scope.processModels = [];
        $scope.predicate = 'id';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.loadAll = function() {
            ProcessModel.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc')]}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.processModels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.createLoanProcess = function(){
            $http.post('/api/processModels/create_test_models?model=loan',{})
                .then(function(res){
                    $scope.loadAll();
                }, function(err){
                    alert('create test model failed.');
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.processModel = {
                name: null,
                comment: null,
                status: null,
                createdAt: null,
                updatedAt: null,
                id: null
            };
        };
    });
