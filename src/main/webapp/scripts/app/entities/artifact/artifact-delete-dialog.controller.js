'use strict';

angular.module('artirestApp')
	.controller('ArtifactDeleteController', function($scope, $uibModalInstance, entity, Artifact) {

        $scope.artifact = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Artifact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
