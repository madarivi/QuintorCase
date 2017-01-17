'use-strict';

angular.module('MusicDatabaseApp')
    .component('artists', {
        templateUrl: 'resources/static/views/artists.template.html',
        controller: function ArtistController($http, restController) {
            var self = this;
            var restUrl = 'api/artists/';

            this.artists = [];
            this.newArtist={artistName:""};

            getArtists();

            this.addArtist = function() {
                restController.addEntity(self.newArtist)
                    .then(function() {
                        self.newArtist.artistName="";
                        updateArtists();
                    });
            };

            this.deleteArtist = function(artist) {
                restController.deleteEntity(artist, artist.artistId).then(updateArtists);
            };

            function getArtists() {
                restController.initEntities(restUrl).then(updateArtists)
            };

            function updateArtists() {
                self.artists = restController.entities;
            }
        }
    });
