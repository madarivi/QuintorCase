package studycase.database.entities;

/**
 * @author Marius
 * 
 * Artist entity class for Hibernate
 * 
 * generates instantiations of associated Album entities with the makeAlbum method
 *  
 */
public class Artist extends Entity{

    private int artistId;
    private String artistName;
    
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
}
