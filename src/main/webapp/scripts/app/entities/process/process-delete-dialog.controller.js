'use strict';

angular.module('artirestApp')
	.controller('ProcessDeleteController', function($scope, $uibModalInstance, entity, Process) {

        $scope.process = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Process.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
