'use-strict';

angular.module('MusicDatabaseApp')
    .component('entities', {
        templateUrl: 'resources/static/views/entities.template.html',
        controller: function EntityController(restController) {
            var self = this;
            var entityTypes = ['artist', 'album', 'song'];

            this.entityType = entityTypes[0];
            this.parentType = '';
            this.nameField = '';
            this.parent = undefined;
            this.filter = undefined;
            this.showAdd = true;

            this.entities = [];
            this.newEntityName = "";

            this.addEntity = function() {
                var newEntity = {};
                newEntity[self.nameField] = self.newEntityName;
                if (this.parent) newEntity[self.parentType] = self.parent;

                restController.addEntity(newEntity)
                    .then(function(response) {
                        self.newEntityName = "";
                        if (!self.entities) self.entities = [];
                        self.entities.push(response);
                    });
            };

            this.deleteEntity = function(entity) {
                restController.deleteEntity(entity[this.entityType + 'Id'])
                    .then(function(response) {
                        self.entities.splice(self.entities.indexOf(entity), 1);
                    });
            };

            this.getEntities = function(parent) {

                var restUrl = 'http://localhost:8080/study-case/api/' + this.entityType + 's/';
                restController.initEntities(restUrl).then(function(response) {
                    self.entities = response;
                    self.nameField = self.entityType + 'Name';
                    self.parent = parent;
                    if (parent) {
                        self.parentType = entityTypes[entityTypes.indexOf(self.entityType) - 1];
                        self.filter = {};
                        self.filter[self.parentType] = {}
                        self.filter[self.parentType][self.parentType + "Id"] = parent[self.parentType+"Id"];
                        self.showAdd = true;
                    } else {
                        self.filter = undefined;
                        self.showAdd = false
                        if (self.entityType == 'artist') self.showAdd = true;
                    }
                })
            }

            this.goInto = function(entity) {
                self.entityType = entityTypes[entityTypes.indexOf(self.entityType) + 1];
                self.getEntities(entity);
            }

            this.getEntities(undefined);
        }
    });
