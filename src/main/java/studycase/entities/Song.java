package studycase.entities;

/**
 * @author Marius
 * 
 * Song entity class for Hibernate
 *  
 */
public class Song extends Entity{

    private int songId;
    private String songName;
    private Album album;
    
    // constructors
    /**
     * Required empty constructor for reflection
     */
    public Song() {}
    
    /**
     * @param albumId
     * @param songName
     */
    public Song(String songName, Album album) {
        this.songName = songName;
        this.album = album;
    }
      
    // getters and setters
    /**
     * @return the songId
     */
    public int getSongId() {
        return songId;
    }
    /**
     * @param songId the songId to set
     */
    public void setSongId(int songId) {
        this.songId = songId;
    }
    /**
     * @return the songName
     */
    public String getSongName() {
        return songName;
    }
    /**
     * @param songName the songName to set
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }
    /**
     * @return the album
     */
    public Album getAlbum() {
        return album;
    }
    /**
     * @param album the album to set
     */
    public void setAlbum(Album album) {
        this.album = album;
    }
}
