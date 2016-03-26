'use strict';

angular.module('artirestApp')
    .factory('Log', function ($resource, DateUtils) {
        return $resource('api/processLogs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
