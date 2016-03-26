'use strict';

angular.module('artirestApp')
	.controller('ArtifactModelDeleteController', function($scope, $uibModalInstance, entity, ArtifactModel) {

        $scope.artifactModel = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ArtifactModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
