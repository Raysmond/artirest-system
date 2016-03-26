'use strict';

angular.module('artirestApp').controller('ProcessDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Process',
        function($scope, $stateParams, $uibModalInstance, entity, Process) {

        $scope.process = entity;
        $scope.load = function(id) {
            Process.get({id : id}, function(result) {
                $scope.process = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('artirestApp:processUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.process.id != null) {
                Process.update($scope.process, onSaveSuccess, onSaveError);
            } else {
                Process.save($scope.process, onSaveSuccess, onSaveError);
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
