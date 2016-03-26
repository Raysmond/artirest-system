'use strict';

angular.module('artirestApp')
	.controller('LogDeleteController', function($scope, $uibModalInstance, entity, Log) {

        $scope.log = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Log.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
