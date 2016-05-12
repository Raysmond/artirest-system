'use strict';

angular.module('artirestApp')
    .controller('ProcessModelDetailController', function ($scope, $rootScope, $stateParams, $timeout, $http, entity, ProcessModel, Process) {
        $scope.processModel = entity;
        $scope.instances = {};

        $scope.load = function (id) {
            ProcessModel.get({id: id}, function(result) {
                $scope.processModel = result;

                //$timeout($scope.showStatesFlowcharts(), 1000);
                setTimeout(function(){
                    $scope.showStatesFlowcharts();
                }, 1000);

                $scope.loadInstances();
            });
        };

        $scope.load($stateParams.id);


        $scope.loadInstances = function(){
            $http.get('/api/processModels/'+$scope.processModel.id+'/processes')
                .then(function(res){
                    $scope.instances = res.data;
                }, function(res){

                });
        };

        $scope.attrTypes = ['String','Long','Integer',"Double",'Float','Text','Date'];

        $scope.saveArtifact = function(artifact){
            console.log(artifact);
        };

        $scope.toggleEditAttr = function(artifact, attr){
            var idx = artifact.attributes.indexOf(attr);
            if(idx==-1) return;
            var key = '#artifact-'+artifact.id + ' tr.artifact-attr';
            var attrRow = $(key)[idx];
        };

        $scope.createProcessInstance = function(){
            $http.post('/api/processModels/'+$scope.processModel.id+'/processes', {})
                .then(function(res){
                    console.log(res);
                    $scope.loadInstances();
                }, function(res){

                });
        };

        var unsubscribe = $rootScope.$on('artirestApp:processModelUpdate', function(event, result) {
            $scope.processModel = result;
        });

        $scope.$on('$destroy', unsubscribe);
        $scope.key = 0;

        $scope.addNode = function(nodes, edges, states, state, x, y, fromKey){
            var node = {
                "key" : $scope.key++,
                "text" : state["comment"],
                "loc" : "" + (x + 130) + " " + y
            };

            if(state["type"] == 'START')
                node["category"] = "Start";
            else if(state["type"] == 'FINAL')
                node["category"] = "End";

            nodes.push(node);


            if(state["type"] != "START"){
                edges.push({
                               "from" : fromKey,
                               "to" : node.key,
                               "fromPort" : x < 100 && state["type"] != "START" ? "B" : "R",
                               "toPort" : "L",
                               "text": 'r1'
                           });
            }

            var maxWidth = $(".artifact-list").width();

            var fromKey = $scope.key - 1;
            for (var i = 0; i < state.nextStates.length; i++) {
                for(var j=0;j<states.length;j++){
                    if(states[j]["name"] == state.nextStates[i]){
                        if(x+280>maxWidth)
                            $scope.addNode(nodes, edges, states, states[j], 20, y + 100 * i + 100, fromKey);
                        else
                            $scope.addNode(nodes, edges, states, states[j], x + 130, y + 100 * i, fromKey);

                    }
                }
            }
        };

        $scope.showStatesFlowcharts = function(){
            $scope.artifacts = $scope.processModel.artifacts;
            for (var i = 0; i < $scope.artifacts.length; i++) {
                var artifact = $scope.artifacts[i];
                var start;
                var states = artifact.states;

                for (var j = 0; j < states.length; j++) {
                    var state = states[j];
                    if(state["type"] == 'START'){
                        start = state;
                    }
                };

                var nodes = [];
                var edges = [];

                $scope.addNode(nodes, edges, states, start, 0, 70, 0);

                var json = {
                    "class": "go.GraphLinksModel",
                    "linkFromPortIdProperty": "fromPort",
                    "linkToPortIdProperty": "toPort",
                    "nodeDataArray": nodes,
                    "linkDataArray": edges
                };

                console.log(json);

                if (json.nodeDataArray.length < 8) {
                    $("#myDiagram-"+artifact.id).css("height", "200px");
                };

                initFlowchart("myDiagram-"+artifact.id);
                loadFlowchartFromJson("myDiagram-"+artifact.id, json);
            };
        };
    });
