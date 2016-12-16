/**
 * 
 */
package studycase.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marius
 * 
 * Artist entity class for Hibernate
 * 
 * generates instantiations of associated Song entities with the makeSong method
 *  
 */
public class Album {
    
    private int albumId;
    private String albumName;
    private Set<Song> songs = new HashSet<>();
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
    public Song makeSong(String songName) {
        Song song = new Song(songName, this);
        songs.add(song);
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
     * @return the set of songs
     */
    public Set<Song> getSongs() {
        return songs;
    }
    /**
     * @param songs the songs to set
     */
    public void setSongs(Set<Song> songs) {
        this.songs = songs;
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
