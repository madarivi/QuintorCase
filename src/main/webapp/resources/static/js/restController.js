'use strict';

angular.module('MusicDatabaseApp')
    .factory('restController', function($http, $q) {

        var factory = {
            restUrl: '',
            entities: [],
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
                    factory.entities = response.data;
                    deferred.resolve();
                }, function error(response) {
                    var prefix = 'error get http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        };

        function addEntity(entity) {
            var deferred = $q.defer();
            $http.post(this.restUrl, entity)
                .then(function success(response) {
                    factory.entities.push(response.data);
                    deferred.resolve();
                }, function error(response) {
                    var prefix = 'error post http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        };

        function deleteEntity(entity, id) {
            var deferred = $q.defer();
            $http.delete(this.restUrl + id)
                .then(function success(response) {
                    if (response.status === 200) {
                        factory.entities.splice(factory.entities.indexOf(entity), 1);
                        deferred.resolve();
                    } else {
                        var prefix = 'error artist delete http request';
                        console.error(prefix, response.status);
                        deferred.reject();
                    }
                },function error(response) {
                    var prefix = 'error artist delete http request';
                    console.error(prefix, response.status);
                    deferred.reject();
                });
            return deferred.promise;
        };
    });