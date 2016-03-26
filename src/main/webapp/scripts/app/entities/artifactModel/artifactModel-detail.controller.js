'use strict';

angular.module('artirestApp')
    .controller('ArtifactModelDetailController', function ($scope, $rootScope, $stateParams, entity, ArtifactModel) {
        $scope.artifactModel = entity;
        $scope.load = function (id) {
            ArtifactModel.get({id: id}, function(result) {
                $scope.artifactModel = result;
            });
        };
        var unsubscribe = $rootScope.$on('artirestApp:artifactModelUpdate', function(event, result) {
            $scope.artifactModel = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
