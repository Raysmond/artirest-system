'use strict';

angular.module('artirestApp')
	.controller('ServiceDeleteController', function($scope, $uibModalInstance, entity, Service) {

        $scope.service = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Service.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
