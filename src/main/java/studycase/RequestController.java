package studycase;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import studycase.entities.Artist;
import studycase.entities.Entity;
import studycase.entities.EntityController;
import studycase.entities.EntityEnum;

@RestController
public class RequestController {
    
    EntityController ec = EntityController.getInstance();
    
    /**
     * Gets a single entity from a table in the database by entity id when a GET request is send to /{table}/{id}
     * 
     * @param table     the table to retrieve from
     * @param id        the id of the entity to retrieve
     * @return          NOT_FOUND if an invalid table name is used
     *                  NO_COTENT if the id is unoccupied or something went wrong in the database access
     *                  OK and the entity otherwise
     */
    @RequestMapping(value = "/{table}/{id}", method = RequestMethod.GET)
    public ResponseEntity<Entity> getEntity(    @PathVariable("table") String table, 
                                                @PathVariable("id") int id  
                                           ) {
        EntityEnum entityEnum = EntityEnum.fromString(table);
        if (entityEnum == null) return new ResponseEntity<Entity>(HttpStatus.NOT_FOUND);
        
        Entity entity = ec.getEntityById(entityEnum.getEntityClass(), id);
        if (entity == null) return new ResponseEntity<Entity>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<Entity>(entity, HttpStatus.OK);
    }
    
    /**
     * Get a list of entities from a table. Search the entity name with the search parameter and restrict to a parentId if required
     * 
     * @param table     the table to retrieve from
     * @param search    the string to search for if supplied
     * @param parentId  the parentId of the entities to get if supplied
     * @return          NOT_FOUND if an invalid table name is used
     *                  NO_CONTENT if something went wrong with the database access
     *                  OK and the list of entities otherwise
     */
    @RequestMapping(value = "/{table}")
    public ResponseEntity<List<Entity>> getEntities(    @PathVariable("table") String table,
                                                        @RequestParam(value="search", defaultValue="") String search,
                                                        @RequestParam(value="parentId", defaultValue="0") int parentId
                                                    ) {
        EntityEnum entityEnum = EntityEnum.fromString(table);
        if (entityEnum == null) return new ResponseEntity<List<Entity>>(HttpStatus.NOT_FOUND);
        List<Entity> entities = null;
        
        switch (entityEnum) {
            case ARTIST:    entities = ec.getArtists(search);
                            break;
            case ALBUM:     entities = ec.getAlbums(search, parentId);
                            break;
            case SONG:      entities = ec.getSongs(search, parentId);
        }
        if (entities == null) return new ResponseEntity<List<Entity>>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<List<Entity>>(entities, HttpStatus.OK);
    }

    @RequestMapping(value = "/artist", method = RequestMethod.POST) 
    public ResponseEntity<Void> createArtist(@RequestBody String artistName, UriComponentsBuilder ucBuilder) {
        
        Artist artist = ec.addArtist(artistName);
        if (artist == null) return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/artist/{id}").buildAndExpand(artist.getArtistId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}