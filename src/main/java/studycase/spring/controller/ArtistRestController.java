package studycase.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studycase.entities.Artist;
import studycase.entities.EntityController;

@RestController
public class ArtistRestController {
    
    @Autowired 
    EntityController entityController;
    
    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> getArtists() {
        return new ResponseEntity<List<Artist>>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    /**
     * Get an Artist from the database
     * 
     * @param id    the id of the artist
     * @return      ResponseEntity with status NOT_FOUND -> 
     *                  the artist was not found or an error occurred
     *              ResponseEntity with status OK and the artist object ->
     *                  the artist was successfully retrieved
     */
    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable("id") int id) {

        Artist artist = (Artist) entityController.getEntityById(Artist.class, id);
        if (artist == null) {
            return new ResponseEntity<Artist>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Artist>(artist, HttpStatus.OK);
    }

    /**
     * Create a new artist in the database
     *
     * @param artist    the artist to add - PRECOND: not null and artistName.length > 0
     * @return          ResponseEntity with status UNPROCESSABLE_ENTITY ->
     *                      the precondition failed
     *                  ResponseEntity with status FAILED_DEPENDENCY ->
     *                      an error occurred
     *                  ResponseEntity with status CREATED and the saved artist object ->
     *                      the artist was successfully created
     */
    @PostMapping(value = "/artists")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artistIn) {
        if (    artistIn == null || 
                artistIn.getArtistName() == null ||
                artistIn.getArtistName().length() == 0) {
            return new ResponseEntity<Artist>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
                
        Artist artist = entityController.addArtist(artistIn.getArtistName());
        if (artist == null) return new ResponseEntity<Artist>(HttpStatus.FAILED_DEPENDENCY);

        return new ResponseEntity<Artist>(artist, HttpStatus.OK);
    }

    /**
     * Delete an artist from the database
     * 
     * @param id    the id of the artist
     * @return      ResponseEntity with status NOT_FOUND ->
     *                  the artist was not found or an error occurred
     *              ResponseEntity with status ACCEPTED ->
     *                  the artist was successfully deleted
     */
    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable int id) {

        if(entityController.deleteArtist(id)) return new ResponseEntity<Void>(HttpStatus.ACCEPTED);

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

    }
}
