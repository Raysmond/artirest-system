'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('artifact', {
                parent: 'entity',
                url: '/artifacts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Artifacts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artifact/artifacts.html',
                        controller: 'ArtifactController'
                    }
                },
                resolve: {
                }
            })
            .state('artifact.detail', {
                parent: 'entity',
                url: '/artifact/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Artifact'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/artifact/artifact-detail.html',
                        controller: 'ArtifactDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Artifact', function($stateParams, Artifact) {
                        return Artifact.get({id : $stateParams.id});
                    }]
                }
            })
            .state('artifact.new', {
                parent: 'artifact',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifact/artifact-dialog.html',
                        controller: 'ArtifactDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    currentState: null,
                                    createdAt: null,
                                    updatedAt: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('artifact', null, { reload: true });
                    }, function() {
                        $state.go('artifact');
                    })
                }]
            })
            .state('artifact.edit', {
                parent: 'artifact',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifact/artifact-dialog.html',
                        controller: 'ArtifactDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Artifact', function(Artifact) {
                                return Artifact.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artifact', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('artifact.delete', {
                parent: 'artifact',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/artifact/artifact-delete-dialog.html',
                        controller: 'ArtifactDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Artifact', function(Artifact) {
                                return Artifact.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('artifact', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
