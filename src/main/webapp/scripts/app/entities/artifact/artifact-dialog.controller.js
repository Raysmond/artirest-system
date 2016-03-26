'use strict';

angular.module('artirestApp').controller('ArtifactDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artifact',
        function($scope, $stateParams, $uibModalInstance, entity, Artifact) {

        $scope.artifact = entity;
        $scope.load = function(id) {
            Artifact.get({id : id}, function(result) {
                $scope.artifact = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('artirestApp:artifactUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.artifact.id != null) {
                Artifact.update($scope.artifact, onSaveSuccess, onSaveError);
            } else {
                Artifact.save($scope.artifact, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreatedAt = {};

        $scope.datePickerForCreatedAt.status = {
            opened: false
        };

        $scope.datePickerForCreatedAtOpen = function($event) {
            $scope.datePickerForCreatedAt.status.opened = true;
        };
        $scope.datePickerForUpdatedAt = {};

        $scope.datePickerForUpdatedAt.status = {
            opened: false
        };

        $scope.datePickerForUpdatedAtOpen = function($event) {
            $scope.datePickerForUpdatedAt.status.opened = true;
        };
}]);
