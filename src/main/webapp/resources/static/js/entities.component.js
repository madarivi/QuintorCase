'use-strict';

angular.module('MusicDatabaseApp')
    .component('entities', {
        templateUrl: 'resources/static/views/entities.template.html',
        controller: function EntityController(restController) {
            var self = this;

            this.entityType = 'artist';
            this.nameField = '';
            // this.parent = {artistId: ''};

            this.entities = [];
            this.newEntityName = "";

            this.addEntity = function() {
                var newEntity = {};
                newEntity[self.nameField] = self.newEntityName;

                restController.addEntity(newEntity)
                    .then(function(response) {
                        self.newEntityName = "";
                        self.entities.push(response);
                    });
            };

            this.deleteEntity = function(entity) {
                restController.deleteEntity(entity[this.entityType + 'Id'])
                    .then(function(response) {
                        self.entities.splice(self.entities.indexOf(entity), 1);
                    });
            };

            this.getEntities = function() {

                var restUrl = 'http://localhost:8080/study-case/api/' + this.entityType + 's/';
                restController.initEntities(restUrl).then(function(response) {
                    self.entities = response;
                    self.nameField = self.entityType + 'Name';
                })
            }

            this.getEntities();
        }
    });
