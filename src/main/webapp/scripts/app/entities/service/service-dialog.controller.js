'use strict';

angular.module('artirestApp').controller('ServiceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', '$http',
        function($scope, $stateParams, $uibModalInstance, entity, $http) {

        $scope.service = entity;
        $scope.inputParams = '';
        if($scope.service.inputParams){
            $scope.inputParams = $scope.service.inputParams.join(',');
        }
        $scope.allowedMethods = ["GET", "POST", "PUT", "DELETE"];
        $scope.allowedServiceTypes = ['HUMAN_TASK','INVOKE_SERVICE'];
        $scope.processModelId = $stateParams.id;

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
}]);
