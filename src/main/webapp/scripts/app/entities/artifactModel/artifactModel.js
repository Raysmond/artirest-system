'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('artifactModel', {
                parent: 'entity',
                url: '/artifactModels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ArtifactModels'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artifactModel/artifactModels.html',
                        controller: 'ArtifactModelController'
                    }
                },
                resolve: {
                }
            })
            .state('artifactModel.detail', {
                parent: 'entity',
                url: '/artifactModel/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ArtifactModel'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artifactModel/artifactModel-detail.html',
                        controller: 'ArtifactModelDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ArtifactModel', function($stateParams, ArtifactModel) {
                        return ArtifactModel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('artifactModel.new', {
                parent: 'artifactModel',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifactModel/artifactModel-dialog.html',
                        controller: 'ArtifactModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    comment: null,
                                    createdAt: null,
                                    updatedAt: null,
                                    attributes: [],
                                    states: [],
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('artifactModel', null, { reload: true });
                    }, function() {
                        $state.go('artifactModel');
                    })
                }]
            })
            .state('artifactModel.edit', {
                parent: 'artifactModel',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifactModel/artifactModel-dialog.html',
                        controller: 'ArtifactModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ArtifactModel', function(ArtifactModel) {
                                return ArtifactModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artifactModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('artifactModel.delete', {
                parent: 'artifactModel',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifactModel/artifactModel-delete-dialog.html',
                        controller: 'ArtifactModelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ArtifactModel', function(ArtifactModel) {
                                return ArtifactModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artifactModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
