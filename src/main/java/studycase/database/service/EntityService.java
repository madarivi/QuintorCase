package studycase.database.service;

import java.util.List;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;

public interface EntityService {
	
	/**
     * Get an entity from the database by id
     * 
     * @param entityClass   class of the entity
     * @param entityId      id (primary key) of the entity
     * @return              the retrieved entity if found in the database, null otherwise
     * 
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract Entity getEntityById(Class<? extends Entity> entityClass, int entityId) throws EntityServiceException;
	
	/**
     * Get a table from the database
     * 
     * @param entityClass   class of the entities to retrieve
     * @return              List of entities
     * 
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract List<Entity> getEntities(Class<? extends Entity> entityClass) throws EntityServiceException;
	
	/**
     * Delete an entity from the database by id
     * 
     * @param entityClass   class of the entity
     * @param entityId      id (primary key) of the entity
     * @return              true if the entity was successfully found and deleted, false otherwise
     *
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract boolean deleteEntityById(Class<? extends Entity> entityClass, int entityId) throws EntityServiceException;
	
	/**
     * Add an artist to the artists table
     * 
     * @param artistName    name of the artist
     * @return              the Artist object 
     * 
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract Artist addArtist(String artistName) throws EntityServiceException;
	
	/**
     * Add an album to the albums table
     * 
     * @param artist artist associated with the album
     * @param albumName name of the album 
     * 
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract Album addAlbum(Artist artist, String albumName) throws EntityServiceException;
	
	/**
     * Add a song to the songs table
     * 
     * @param album album associated with the song
     * @param songName name of the song 
     * 
     * @throws EntityServiceException, if something went wrong getting the entities
     * 
     */
	public abstract Song addSong(Album album, String songName) throws EntityServiceException;
}
