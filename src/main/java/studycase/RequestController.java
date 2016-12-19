package studycase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import studycase.entities.Album;
import studycase.entities.Artist;
import studycase.entities.Entity;
import studycase.entities.EntityController;
import studycase.entities.Song;

@RestController
public class RequestController {
    
    EntityController ec = new EntityController();

    // get single entries //
    private <T extends Entity> ResponseEntity<T> getEntity(Class<T> cls, int id) {
        T t = ec.getEntityById(cls, id);
        if (t == null) return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<T>(t, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/artists/{artistId}", method = RequestMethod.GET)
    public ResponseEntity<Artist> getArtist(@PathVariable("artistId") int artistId) {
        return getEntity(Artist.class, artistId);
    }
    
    @RequestMapping(value = "/albums/{albumId}", method = RequestMethod.GET)
    public ResponseEntity<Album> getAlbum(@PathVariable("albumId") int albumId) {
        return getEntity(Album.class, albumId);
    }
    
    @RequestMapping(value = "/songs/{songId}", method = RequestMethod.GET)
    public ResponseEntity<Song> getSong(@PathVariable("songId") int songId) {
        return getEntity(Song.class, songId);
    }
    
}