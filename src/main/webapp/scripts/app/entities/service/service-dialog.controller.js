'use strict';

angular.module('artirestApp').controller('ServiceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', '$http',
        function($scope, $stateParams, $uibModalInstance, entity, $http) {

        $scope.service = entity;
        $scope.inputParams = $scope.service.inputParams.join(',');
        $scope.allowedMethods = ["GET", "POST", "PUT", "DELETE"];

        $scope.processModelId = $stateParams.id;
        $scope.load = function(id) {
            Service.get({id : id}, function(result) {
                $scope.service = result;
            });
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if($scope.inputParams){
                $scope.service.inputParams = $scope.inputParams.split(',');
            } else {
                $scope.service.inputParams = [];
            }

            $uibModalInstance.close($scope.service);
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
