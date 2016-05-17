'use strict';

angular.module('artirestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('service', {
                parent: 'entity',
                url: '/services',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Services'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/service/services.html',
                        controller: 'ServiceController'
                    }
                },
                resolve: {
                }
            })
            .state('service.detail', {
                parent: 'entity',
                url: '/service/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Service'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/service/service-detail.html',
                        controller: 'ServiceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Service', function($stateParams, Service) {
                        return Service.get({id : $stateParams.id});
                    }]
                }
            })
            .state('service.new', {
                parent: 'service',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/service/service-dialog.html',
                        controller: 'ServiceDialogController',
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
                        $state.go('service', null, { reload: true });
                    }, function() {
                        $state.go('service');
                    })
                }]
            })
            .state('service.edit', {
                parent: 'service',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/service/service-dialog.html',
                        controller: 'ServiceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Service', function(Service) {
                                return Service.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('service', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('service.delete', {
                parent: 'service',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/service/service-delete-dialog.html',
                        controller: 'ServiceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Service', function(Service) {
                                return Service.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('service', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
