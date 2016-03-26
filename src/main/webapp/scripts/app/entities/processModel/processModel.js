'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('processModel', {
                parent: 'entity',
                url: '/processModels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ProcessModels'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/processModel/processModels.html',
                        controller: 'ProcessModelController'
                    }
                },
                resolve: {
                }
            })
            .state('processModel.detail', {
                parent: 'entity',
                url: '/processModel/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ProcessModel'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/processModel/processModel-detail.html',
                        controller: 'ProcessModelDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ProcessModel', function($stateParams, ProcessModel) {
                        //return ProcessModel.get({id : $stateParams.id});
                        return {};
                    }]
                }
            })
            .state('processModel.new', {
                parent: 'processModel',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/processModel/processModel-dialog.html',
                        controller: 'ProcessModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    comment: null,
                                    status: 'DESIGNING',
                                    createdAt: null,
                                    updatedAt: null,
                                    services: [],
                                    businessRules: [],
                                    artifacts: [],
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('processModel', null, { reload: true });
                    }, function() {
                        $state.go('processModel');
                    })
                }]
            })
            .state('processModel.edit', {
                parent: 'processModel',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/processModel/processModel-dialog.html',
                        controller: 'ProcessModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProcessModel', function(ProcessModel) {
                                return ProcessModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('processModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('processModel.delete', {
                parent: 'processModel',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/processModel/processModel-delete-dialog.html',
                        controller: 'ProcessModelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProcessModel', function(ProcessModel) {
                                return ProcessModel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('processModel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
