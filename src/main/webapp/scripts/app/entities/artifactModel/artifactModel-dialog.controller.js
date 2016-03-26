'use strict';

angular.module('artirestApp').controller('ArtifactModelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArtifactModel',
        function($scope, $stateParams, $uibModalInstance, entity, ArtifactModel) {

        $scope.artifactModel = entity;
        $scope.attribute = {name: '', comment: '', type: 'String'};
        $scope.state = {name: '', comment: '', type: 'NORMAL', nextStates: ''};

        $scope.attributeTypes = ["String", "Integer", "Double", "Text"];
        $scope.stateTypes = ['NORMAL', 'START', 'FINAL'];

        $scope.load = function(id) {
            ArtifactModel.get({id : id}, function(result) {
                $scope.artifactModel = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('artirestApp:artifactModelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.artifactModel.id != null) {
                ArtifactModel.update($scope.artifactModel, onSaveSuccess, onSaveError);
            } else {
                ArtifactModel.save($scope.artifactModel, onSaveSuccess, onSaveError);
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

        $scope.addAttribute = function(attribute){
            $scope.artifactModel.attributes.push(attribute);
            $scope.attribute = {name: '', comment: '', type: 'String'};
        };

        $scope.addState = function(state){
            state.nextStates = state.nextStates.split(',');
            $scope.artifactModel.states.push(state);
            $scope.state = {name: '', comment: '', type: 'NORMAL', nextStates: ''};
        };

        $scope.removeAttribute = function(attribute){
            var index = $scope.artifactModel.attributes.indexOf(attribute);
            if(index>=0){
                $scope.artifactModel.attributes.splice(index, 1);
            }
        };


        $scope.removeState = function(state){
            var index = $scope.artifactModel.states.indexOf(state);
            if(index>=0){
                $scope.artifactModel.states.splice(index, 1);
            }
        };
}]);
