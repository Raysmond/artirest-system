'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('process', {
                parent: 'entity',
                url: '/processs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Processes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/process/processs.html',
                        controller: 'ProcessController'
                    }
                },
                resolve: {
                }
            })
            .state('process.detail', {
                parent: 'entity',
                url: '/process/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Process'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/process/process-detail.html',
                        controller: 'ProcessDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Process', function($stateParams, Process) {
                        //return Process.get({id : $stateParams.id});
                        return {};
                    }]
                }
            })
            .state('process.new', {
                parent: 'process',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/process/process-dialog.html',
                        controller: 'ProcessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    isRunning: null,
                                    createdAt: null,
                                    updatedAt: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('process', null, { reload: true });
                    }, function() {
                        $state.go('process');
                    })
                }]
            })
            .state('process.edit', {
                parent: 'process',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/process/process-dialog.html',
                        controller: 'ProcessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Process', function(Process) {
                                return Process.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('process', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('process.delete', {
                parent: 'process',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/process/process-delete-dialog.html',
                        controller: 'ProcessDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Process', function(Process) {
                                return Process.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('process', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
