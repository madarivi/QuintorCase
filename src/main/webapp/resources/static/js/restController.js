'use strict';

angular.module('MusicDatabaseApp')
    .factory('restController', function($http, $q) {

        var factory = {
            restUrl: '',
            initEntities: initEntities,
            addEntity: addEntity,
            deleteEntity: deleteEntity
        };
        return factory;

        function initEntities(restUrl) {
            this.restUrl = restUrl;
            var deferred = $q.defer();
            $http.get(this.restUrl)
                .then(function success(response) {
                    deferred.resolve(response.data);
                }, function error(response) {
                    var prefix = 'error get http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        }

        function addEntity(entity) {
            var deferred = $q.defer();
            $http.post(this.restUrl, entity)
                .then(function success(response) {
                    deferred.resolve(response.data);
                }, function error(response) {
                    var prefix = 'error post http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        };

        function deleteEntity(id) {
            var deferred = $q.defer();
            $http.delete(this.restUrl + id)
                .then(function success(response) {
                    if (response.status === 200) {
                        deferred.resolve(response.data);
                    } else {
                        var prefix = 'error delete http request';
                        console.error(prefix, response.status);
                        deferred.reject();
                    }
                },function error(response) {
                    var prefix = 'error delete http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        };
    });