'use strict';

angular.module('artirestApp')
	.controller('ProcessModelDeleteController', function($scope, $uibModalInstance, entity, ProcessModel) {

        $scope.processModel = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProcessModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
