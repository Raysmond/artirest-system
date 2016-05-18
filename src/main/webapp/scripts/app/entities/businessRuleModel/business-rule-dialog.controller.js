'use strict';

angular.module('artirestApp').controller('BusinessRuleModelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'processModel', '$http',
        function($scope, $stateParams, $uibModalInstance, entity, processModel, $http) {
        $scope.businessRule = entity;

        $scope.save = function () {
            $scope.isSaving = true;
            $uibModalInstance.close($scope.businessRule);
        };

        $scope.processModel = processModel;

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.allowedArtifacts = [];
        for(var i=0;i<$scope.processModel.artifacts.length;i++){
            $scope.allowedArtifacts.push($scope.processModel.artifacts[i].name);
        }

        $scope.allowedRuleType = ["INSTATE","ATTRIBUTE_DEFINED","SCALAR_COMPARISON"];

        $scope.newRule = {
            type:null,
            artifact: null,
            attribute: null,
            state: null,
            value: null
        };
        $scope.postNewRule = {
            type:null,
            artifact: null,
            attribute: null,
            state: null,
            value: null
        };
        $scope.addNewRuleToPreConditions = function(){
            $scope.addNewRule($scope.businessRule.preConditions, $scope.newRule);
            $scope.newRule = {
                type:null,
                artifact: null,
                attribute: null,
                state: null,
                value: null
            };
        };
        $scope.addNewRuleToPostConditions = function(){
            $scope.addNewRule($scope.businessRule.postConditions, $scope.postNewRule);
            $scope.postNewRule = {
                type:null,
                artifact: null,
                attribute: null,
                state: null,
                value: null
            };
        };

        $scope.addNewRule = function(conditions, rule){
            if(rule.type && rule.artifact){
                var newRule = {
                    type:rule.type,
                    artifact: rule.artifact,
                    attribute: rule.attribute,
                    state: rule.state,
                    value: rule.value
                };
                conditions.push(newRule);
            }
        };

        $scope.newTransition ={
            artifact: null, fromState: null, toState: null
        };
        $scope.addNewTransition = function(){
            if($scope.newTransition.artifact &&$scope.newTransition.fromState &&$scope.newTransition.toState){
                $scope.businessRule.action.transitions.push($scope.newTransition);
                $scope.newTransition ={
                    artifact: null, fromState: null, toState: null
                };
            }
        };

}]);
