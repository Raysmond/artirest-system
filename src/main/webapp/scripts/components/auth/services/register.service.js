'use strict';

angular.module('artirestApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


