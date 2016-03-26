'use strict';

angular.module('artirestApp').controller('LogDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Log',
        function($scope, $stateParams, $uibModalInstance, entity, Log) {

        $scope.log = entity;
        $scope.load = function(id) {
            Log.get({id : id}, function(result) {
                $scope.log = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('artirestApp:logUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.log.id != null) {
                Log.update($scope.log, onSaveSuccess, onSaveError);
            } else {
                Log.save($scope.log, onSaveSuccess, onSaveError);
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
}]);
