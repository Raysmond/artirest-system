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
        $scope.allowedOperators = ["EQUAL","LARGER","LESS"];
        $scope.newRule = {
            type:null,
            artifact: null,
            attribute: null,
            state: null,
            value: null,
            operator: null
        };
        $scope.postNewRule = {
            type:null,
            artifact: null,
            attribute: null,
            state: null,
            value: null,
            operator: null
        };
        $scope.addNewRuleToPreConditions = function(){
            $scope.addNewRule($scope.businessRule.preConditions, $scope.newRule, $scope.editNewPreRuleIdx);
            $scope.newRule = {
                type:null,
                artifact: null,
                attribute: null,
                state: null,
                value: null,
                operator: null
            };
            $scope.editNewPreRuleIdx = -1;
        };
        $scope.addNewRuleToPostConditions = function(){
            $scope.addNewRule($scope.businessRule.postConditions, $scope.postNewRule, $scope.editNewPostRuleIdx);
            $scope.postNewRule = {
                type:null,
                artifact: null,
                attribute: null,
                state: null,
                value: null,
                operator: null
            };
            $scope.editNewPostRuleIdx = -1;
        };

        $scope.addNewRule = function(conditions, rule, index){
            if(rule.type && rule.artifact){
                var newRule = {
                    type:rule.type,
                    artifact: rule.artifact,
                    attribute: rule.attribute,
                    state: rule.state,
                    value: rule.value,
                    operator: rule.operator
                };
                if(index===-1)
                    conditions.push(newRule);
            }
        };

        $scope.editNewPreRuleIdx = -1;
        $scope.editPreAtom = function(atom){
            $scope.newRule = atom;
            $scope.editNewPreRuleIdx = $scope.businessRule.preConditions.indexOf(atom);
        };

        $scope.editNewPostRuleIdx = -1;
        $scope.editPostAtom = function(atom){
            $scope.postNewRule = atom;
            $scope.editNewPostRuleIdx = $scope.businessRule.postConditions.indexOf(atom);
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
