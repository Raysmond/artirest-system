'use strict';

angular.module('artirestApp').controller('ProcessModelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProcessModel',
        function($scope, $stateParams, $uibModalInstance, entity, ProcessModel) {

        $scope.processModel = entity;
        $scope.load = function(id) {
            ProcessModel.get({id : id}, function(result) {
                $scope.processModel = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('artirestApp:processModelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.processModel.id != null) {
                ProcessModel.update($scope.processModel, onSaveSuccess, onSaveError);
            } else {
                ProcessModel.save($scope.processModel, onSaveSuccess, onSaveError);
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
