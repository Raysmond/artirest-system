'use strict';

angular.module('artirestApp')
    .controller('LogDetailController', function ($scope, $rootScope, $stateParams, entity, Log) {
        $scope.log = entity;
        $scope.load = function (id) {
            Log.get({id: id}, function(result) {
                $scope.log = result;
            });
        };
        var unsubscribe = $rootScope.$on('artirestApp:logUpdate', function(event, result) {
            $scope.log = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
