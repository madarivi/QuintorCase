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
	
	public abstract boolean deleteEntityById(Class<? extends Entity> entityClass, int entityId);
	
	public abstract Artist addArtist(String artistName);
	
	public abstract Album addAlbum(Artist artist, String albumName);
	
	public abstract Song addSong(Album album, String songName);
}
