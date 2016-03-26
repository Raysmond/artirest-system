'use strict';

angular.module('artirestApp')
    .controller('ProcessDetailController', function ($scope, $rootScope, $stateParams, $http, entity, Process, ProcessModel) {
        $scope.process = entity;
        $scope.availableServices = [];
        $scope.processModel = {};
        $scope.currentArtifactForService = {};
        $scope.logs = [];

        $scope.load = function (id) {
            Process.get({id: id}, function(result) {
                $scope.process = result;
                $scope.processModel = $scope.process.processModel;
                $scope.loadAvailableServices();
                $scope.loadLogs();
            });
        };

        $scope.load($stateParams.id);

        $scope.refresh = function(){
            $scope.load($stateParams.id);
        };

        $scope.loadAvailableServices = function(){
            $http.get('/api/processes/'+$stateParams.id+'/available_services')
                .then(function(res){
                    $scope.availableServices = res.data;
                },function(res){
                    // error
                });
        };

        $scope.loadLogs = function(){
            $http.get('/api/processes/'+$stateParams.id+'/processLogs')
                .then(function(res){
                    $scope.logs = res.data;
                }, function(res){
                    alert('Failed to load process logs');
                });
        };

        $scope.$watch('logs', function(){
            for (var i = 0; i < $scope.process.artifacts.length; i++) {
                var artifact = $scope.process.artifacts[i];

                $scope.showArtifactLifeCycle(artifact, $scope.logs);
            }
        });


        var unsubscribe = $rootScope.$on('artirestApp:processUpdate', function(event, result) {
            $scope.process = result;
        });
        $scope.$on('$destroy', unsubscribe);

        var findProcessArtifact = function(name){
            for(var i=0;i<$scope.process.artifacts.length;i++){
                var artifact = $scope.process.artifacts[i];

                if (artifact.name == name){
                    return artifact;
                }
            }

            return undefined;
        };

        var initArtifact = function(name){
            var artifactModel;
            for (var i=0;i<$scope.processModel.artifacts.length;i++){
                var model = $scope.processModel.artifacts[i];
                if (model.name == name){
                    artifactModel = model;
                    break;
                }
            }

            var artifact = {
                name: name,
                attributes: []
            };

            if(artifactModel != undefined){

                for (var i = 0; i < artifactModel.attributes.length; i++){
                    var attr = artifactModel.attributes[i];
                    artifact.attributes.push({
                                        name: attr.name,
                                        comment: attr.comment,
                                        value: ''
                                    });
                }
            }

            return artifact;
        };

        $scope.isServiceParam = function(service, attribute_name){
            return service.inputParams.indexOf(attribute_name) >= 0;
        };

        $scope.showService = function(service){
            $scope.currentService = service;

            var artifact = findProcessArtifact(service.inputArtifact);

            if (artifact != undefined){
                $scope.currentArtifactForService = artifact;
            } else {
                $scope.currentArtifactForService = initArtifact(service.inputArtifact);
            }


        };

        $scope.invokeService = function(service){
            var url = '/api/processes/'+$stateParams.id+'/services/'+service.name;
            console.log($scope.currentArtifactForService);

            $http.post(url, $scope.currentArtifactForService)
                .then(function(res){
                    console.log(res);

                    $scope.refresh();
                    $scope.currentService = undefined;
                    $scope.currentArtifactForService = undefined;
                }, function(res){
                    // error
                    alert('Failed to invoke service ' + service.name);
                });
        };


        // ------------------------------

        $scope.findStateComment = function(artifactInstance, stateName){
            var states = [];
            for (var i = 0; i < $scope.processModel.artifacts.length; i++) {
                var artifact = $scope.processModel.artifacts[i];
                if (artifact.name == artifactInstance.name) {
                    states = artifact.states;
                    break;
                };
            };

            // console.log(states);

            for (var i = 0; i < states.length; i++) {
                if(states[i].name == stateName){
                    return states[i].comment;
                }
            };

            return stateName;
        };

        $scope.findStateModel = function(artifactInstance, stateName){
            var state = undefined;

            var states = [];
            for (var i = 0; i < $scope.processModel.artifacts.length; i++) {
                var artifact = $scope.processModel.artifacts[i];
                if (artifact.name == artifactInstance.name) {
                    states = artifact.states;
                    break;
                };
            };

            // console.log(states);

            for (var i = 0; i < states.length; i++) {
                if(states[i].name == stateName){
                    return states[i];
                }
            };

            return state;
        };

        $scope.showArtifactLifeCycle = function(artifact, logs){
            var transitions = [];
            for (var i = 0; i < logs.length; i++) {
                if(logs[i].artifactId == artifact.id && logs[i].type == "STATE_TRANSITION"){
                    transitions.push({
                                         from: logs[i].fromState,
                                         to: logs[i].toState,
                                     });
                }
            }

            var nodes = [];
            var edges = [];
            var key = 0;
            var x = 0, y = 0;
            var curState;
            var curNode;

            var alignmentRight = transitions.length <= 3;

            if (transitions.length == 0) {
                var startNode = {
                    key: key++,
                    text: $scope.findStateComment(artifact, "start"),
                    loc: "" + x+ " " + y,
                    category: 'Start'
                };

                nodes.push(startNode);
            };

            for (var i = 0; i < transitions.length; i++) {
                var transition = transitions[i];
                if (transition.from == "start") {
                    var startNode = {
                        key: key++,
                        text: $scope.findStateComment(artifact, transition.from),
                        loc: "" + x+ " " + y,
                        category: 'Start'
                    }
                    var secondNode = {
                        key: key++,
                        text: $scope.findStateComment(artifact, transition.to),
                        loc: "" + (x+120) + " "+ y
                    };

                    x = x + 120;

                    // if (!alignmentRight) {
                    //   x = x - 120;
                    //   y = y + 80;
                    //   secondNode.loc = "" + x + " " + y;
                    // };

                    nodes.push(startNode);
                    nodes.push(secondNode);
                    edges.push({
                                   from: startNode.key,
                                   to: secondNode.key,
                                   fromPort: 'R',
                                   toPort: 'L'
                               });
                    curState = transition.to;
                    curNode = secondNode;

                    break;
                };
            };

            var maxWidth = $('.artifact-instance').width();

            while(true){
                var transition = undefined;
                for (var i = 0; i < transitions.length; i++) {

                    if(transitions[i].from == curState){

                        var stateModel = $scope.findStateModel(artifact, transitions[i].to);
                        transition = transitions[i];
                        var node = {
                            key: key++,
                            text: stateModel.comment,
                            loc: "" + (x+120)+ " "+y
                        };

                        if (stateModel.type == "FINAL") {
                            node["category"] = "End";
                        };

                        x = x + 120;

                        // if (!alignmentRight) {
                        //   x = x - 120;
                        //   y = y + 80;
                        //   node.loc = "" + x + " " + y;
                        // };

                        var nextLine = false;
                        if (x >= maxWidth - 80) {
                            x = 0;
                            y = y + 100;
                            node.loc = "" + x + " " + y;
                            nextLine = true;
                        };


                        nodes.push(node);
                        edges.push({
                                       from: curNode.key,
                                       to: node.key,
                                       fromPort: !nextLine ? 'R' : 'B',
                                       toPort: !nextLine ? 'L' : 'T'
                                   });

                        curNode = node;
                        curState = transitions[i].to;
                    }

                };

                if (transition == undefined) {
                    break;
                };
            }

            var json = {
                "class": "go.GraphLinksModel",
                "linkFromPortIdProperty": "fromPort",
                "linkToPortIdProperty": "toPort",
                "nodeDataArray": nodes,
                "linkDataArray": edges
            };

            console.log(json);

            if (json.nodeDataArray.length > 8) {
                $("#myDiagram-"+artifact.id).css("height", "400px")
            };

            initFlowchart("myDiagram-"+artifact.id);
            loadFlowchartFromJson("myDiagram-"+artifact.id, json);
        };

    });
