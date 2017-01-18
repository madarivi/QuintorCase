package studycase.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.EntityEnum;
import studycase.database.entities.Song;
import studycase.database.service.EntityService;

@CrossOrigin(origins = "http://localhost:5555")
@RestController
public class EntityRestController {
    
    @Autowired 
    EntityService entityService;
    
    /**
     * Get all entities from a table
     * 
     * @param table     the name of the table to retrieve from
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      table was not recognized
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status NO_CONTENT -> 
     *                      the entity list was empty
     *                  ResponseEntity with status OK and the list of Entity objects ->
     *                      the table was successfully retrieved
     */
    @GetMapping("/api/{table}")
    public ResponseEntity<List<Entity>> getEntities(@PathVariable("table") String table) {
        
        EntityEnum entityEnum = EntityEnum.fromString(table);
        if (entityEnum == null) return new ResponseEntity<List<Entity>>(HttpStatus.BAD_REQUEST);
        
        List<Entity> entities;
        try {
            entities = entityService.getEntities(entityEnum.getEntityClass());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<Entity>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (entities.size() == 0) {
            return new ResponseEntity<List<Entity>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Entity>>(entities, HttpStatus.OK);
    }
    
    /**
     * Get an entity from the database
     * 
     * @param table     the name of the table to retrieve from
     * @param id        the id of the entity
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      table was not recognized
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status NO_CONTENT -> 
     *                      the entity was not found
     *                  ResponseEntity with status OK and the Entity object ->
     *                      the entity was successfully retrieved
     */
    @GetMapping("/api/{table}/{id}")
    public ResponseEntity<Entity> getEntity(    @PathVariable("table") String table,
                                                @PathVariable("id") int id) {
        
        EntityEnum entityEnum = EntityEnum.fromString(table);
        if (entityEnum == null) return new ResponseEntity<Entity>(HttpStatus.BAD_REQUEST);
        
        Entity entity;
        try {
            entity = entityService.getEntityById(entityEnum.getEntityClass(), id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Entity>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (entity == null) {
            return new ResponseEntity<Entity>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Entity>(entity, HttpStatus.OK);
    }

    /**
     * Create a new artist in the database
     *
     * @param artist    the artist to add - precondition: not null AND artistName.length > 0
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      the precondition failed
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status CREATED and the saved artist object ->
     *                      the artist was successfully created
     */
    @PostMapping(value = "/api/artists")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artistIn) {
        
        if (    artistIn == null || 
                artistIn.getArtistName() == null ||
                artistIn.getArtistName().length() == 0) {
            return new ResponseEntity<Artist>(HttpStatus.BAD_REQUEST);
        }
        
        Artist artist;
        try {
            artist = entityService.addArtist(artistIn.getArtistName());
        }
        catch (Exception e) {
            return new ResponseEntity<Artist>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
            
        return new ResponseEntity<Artist>(artist, HttpStatus.CREATED);
    }
    
    /**
     * Create a new album in the database
     *
     * @param album     the album to add - precondition: not null AND albumName.length > 0
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      the precondition failed
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status CREATED and the saved album object ->
     *                      the album was successfully created
     */
    @PostMapping(value = "/api/albums")
    public ResponseEntity<Album> createAlbum(@RequestBody Album albumIn) {
        
        if (    albumIn == null || 
                albumIn.getAlbumName() == null ||
                albumIn.getAlbumName().length() == 0) {
            return new ResponseEntity<Album>(HttpStatus.BAD_REQUEST);
        }
        
        Artist artist;
        Album album;
        try {
            artist = (Artist) entityService.getEntityById(Artist.class, albumIn.getArtist().getArtistId());
            if (artist == null) return new ResponseEntity<Album>(HttpStatus.BAD_REQUEST);
            album = entityService.addAlbum(artist, albumIn.getAlbumName());
        }
        catch (Exception e) {
            return new ResponseEntity<Album>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
            
        return new ResponseEntity<Album>(album, HttpStatus.CREATED);
    }
    
    /**
     * Create a new song in the database
     *
     * @param song     the song to add - precondition: not null AND songName.length > 0
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      the precondition failed
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status CREATED and the saved song object ->
     *                      the song was successfully created
     */
    @PostMapping(value = "/api/songs")
    public ResponseEntity<Song> createSong(@RequestBody Song songIn) {
        
        if (    songIn == null || 
                songIn.getSongName() == null ||
                songIn.getSongName().length() == 0) {
            return new ResponseEntity<Song>(HttpStatus.BAD_REQUEST);
        }
        
        Album album;
        Song song;
        try {
            album = (Album) entityService.getEntityById(Album.class, songIn.getAlbum().getAlbumId());
            if (album == null) return new ResponseEntity<Song>(HttpStatus.BAD_REQUEST);
            song = entityService.addSong(album, songIn.getSongName());
        }
        catch (Exception e) {
            return new ResponseEntity<Song>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
            
        return new ResponseEntity<Song>(song, HttpStatus.CREATED);
    }
   
    /**
     * Delete an entity from the database
     * 
     * @param table     the name of the table to delete from
     * @param id        the id of the entity
     * @return          ResponseEntity with status BAD_REQUEST ->
     *                      table was not recognized
     *                  ResponseEntity with status INTERNAL_SERVER_ERROR ->
     *                      something went wrong with the database access
     *                  ResponseEntity with status NO_CONTENT -> 
     *                      the entity was not found
     *                  ResponseEntity with status OK and the Entity object ->
     *                      the entity was successfully deleted
     */
    @DeleteMapping("/api/{table}/{id}")
    public ResponseEntity<Void> deleteArtist(   @PathVariable("table") String table,
                                                @PathVariable("id") int id
                                            ) {
        
            EntityEnum entityEnum = EntityEnum.fromString(table);
            if (entityEnum == null) return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            
            boolean deleted;
            try {
                deleted = entityService.deleteEntityById(entityEnum.getEntityClass(), id);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (!deleted) {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
