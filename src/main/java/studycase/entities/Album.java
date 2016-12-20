/**
 * 
 */
package studycase.entities;

/**
 * @author Marius
 * 
 * Artist entity class for Hibernate
 * 
 * generates instantiations of associated Song entities with the makeSong method
 *  
 */
public class Album extends Entity{
    
    private int albumId;
    private String albumName;
    private Artist artist;

    // constructors
    /**
     * Required empty constructor for reflection
     */
    public Album() {}
    /**
     * @param artistId
     * @param albumName
     * @throws IllegalArgumentException if artistId <= 0
     */
    Album(String albumName, Artist artist) {
        this.albumName = albumName;
        this.artist = artist;
    }
    
    // methods
    /**
     * @param songName the name of the song
     * @return Song entity with name songName associated with this Album entity
     */
    Song makeSong(String songName) {
        Song song = new Song(songName, this);
        return song;
    }
    
    // getters and setters
    /**
     * @return the albumId
     */
    public Integer getAlbumId() {
        return albumId;
    }
    /**
     * @param albumId the albumId to set
     */
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }
    /**
     * @return the albumName
     */
    public String getAlbumName() {
        return albumName;
    }
    /**
     * @param albumName the albumName to set
     */
    public void setAlbumName(String albumName) {    
        this.albumName = albumName;
    }
    /**
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }
    /**
     * @param artist the artist to set
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
