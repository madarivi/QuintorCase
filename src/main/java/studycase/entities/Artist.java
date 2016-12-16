package studycase.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marius
 * 
 * Artist entity class for Hibernate
 * 
 * generates instantiations of associated Album entities with the makeAlbum method
 *  
 */
public class Artist {

    private int artistId;
    private String artistName;
    private Set<Album> albums = new HashSet<>();
    
    // constructors
    /**
     * Required empty constructor for reflection
     */
    public Artist() {};
    /**
     * @param artistName
     */
    public Artist(String artistName) {
        this.artistName = artistName;
    }
    
    // methods
    /**
     * @param albumName the name of the album
     * @return Album entity with name albumName associated with this Artist entity,
     * returns null if the album allready exists
     */
    public Album makeAlbum(String albumName){
        Album album = new Album(albumName, this);
        albums.add(album);
        return album;
    }
    
    // getters and setters
    /**
     * @return the artistId
     */
    public int getArtistId() {
        return artistId;
    }
    /**
     * @param artistId the artistId to set
     */
    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }
    /**
     * @return the artistName
     */
    public String getArtistName() {
        return artistName;
    }
    /**
     * @param artistName the artistName to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    /**
     * @return the albums
     */
    public Set<Album> getAlbums() {
        return albums;
    }
    /**
     * @param albums the albums to set
     */
    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
