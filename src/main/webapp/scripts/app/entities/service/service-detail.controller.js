'use strict';

angular.module('artirestApp')
    .controller('ServiceDetailController', function ($scope, $rootScope, $stateParams, entity, Service) {
        $scope.service = entity;
        $scope.load = function (id) {
            Service.get({id: id}, function(result) {
                $scope.service = result;
            });
        };
        var unsubscribe = $rootScope.$on('artirestApp:serviceUpdate', function(event, result) {
            $scope.service = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
