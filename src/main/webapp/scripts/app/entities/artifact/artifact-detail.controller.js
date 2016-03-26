'use strict';

angular.module('artirestApp')
    .controller('ArtifactDetailController', function ($scope, $rootScope, $stateParams, entity, Artifact) {
        $scope.artifact = entity;
        $scope.load = function (id) {
            Artifact.get({id: id}, function(result) {
                $scope.artifact = result;
            });
        };
        var unsubscribe = $rootScope.$on('artirestApp:artifactUpdate', function(event, result) {
            $scope.artifact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
