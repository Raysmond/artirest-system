'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('log', {
                parent: 'entity',
                url: '/logs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Logs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/log/logs.html',
                        controller: 'LogController'
                    }
                },
                resolve: {
                }
            })
            .state('log.detail', {
                parent: 'entity',
                url: '/log/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Log'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/log/log-detail.html',
                        controller: 'LogDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Log', function($stateParams, Log) {
                        return Log.get({id : $stateParams.id});
                    }]
                }
            })
            .state('log.new', {
                parent: 'log',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/log/log-dialog.html',
                        controller: 'LogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    processId: null,
                                    artifactId: null,
                                    fromState: null,
                                    toState: null,
                                    service: null,
                                    memo: null,
                                    createdAt: null,
                                    type: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('log', null, { reload: true });
                    }, function() {
                        $state.go('log');
                    })
                }]
            })
            .state('log.edit', {
                parent: 'log',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/log/log-dialog.html',
                        controller: 'LogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Log', function(Log) {
                                return Log.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('log', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('log.delete', {
                parent: 'log',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/log/log-delete-dialog.html',
                        controller: 'LogDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Log', function(Log) {
                                return Log.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('log', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
