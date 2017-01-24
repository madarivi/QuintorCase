package studycase.database.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;

/**
 * @author Marius
 * 
 * Controller for Hibernate entities
 *
 */
@Service
public class EntityServiceImpl implements EntityService, InitializingBean, DisposableBean{
    
    private ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;
    
    
    // get from the database //
    @Override
    public Entity getEntityById(Class<? extends Entity> entityClass, int entityId) throws EntityServiceException{
        Entity entity = null;
        
        Session session = null;
        try {
        	session = sessionFactory.openSession();
	        session.beginTransaction();
	        entity = (Entity) session.get(entityClass, entityId);
	        session.getTransaction().commit();
	        session.close();
        }
        catch (Exception e) {
        	System.err.println(e);
        	if (session != null) session.getTransaction().rollback();
        	throw new EntityServiceException(e);
        }
        finally {
        	if (session != null) session.close();
        }
        
        return entity;
    }
    
    @SuppressWarnings("unchecked")
    public List<Entity> getEntities(Class<? extends Entity> entityClass) throws EntityServiceException{
        List<Entity> entities;
        String queryString = "from " + entityClass.getSimpleName();
        
        Session session = null;
        try {
        	session = sessionFactory.openSession();
	        session.beginTransaction();
	        Query query = session.createQuery(queryString);
	        entities = query.list();
	        session.getTransaction().commit();
        }
        catch(Exception e) {
        	System.err.println(e);
        	if (session != null) session.getTransaction().rollback();
        	throw new EntityServiceException(e);
        }
        finally {
        	if (session != null) session.close();
        }
        
        return entities;
    }
    
    /**
     * Get a list of entities from the database from an HQL query
     * 
     * @param queryString the HQL query
     * @return List of entities, null if a database access error occurs
     */
    @SuppressWarnings("unchecked")
    private List<Entity> getEntitiesByQuery(String queryString) {
        Session session = sessionFactory.openSession();
        List<Entity> entities = null;
        
        try {
            Query query = session.createQuery(queryString);
            entities = query.list();
        }
        catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }
        return entities;
    }
    
    /**
     * Get artists from the database
     * 
     * @param search    the string to search for in artistName
     * @return          List of artist entities, null if a database access error occurs
     */
    public List<Entity> getArtists(String search) {
        String queryString = "from Artist";
        if (search != null && !search.isEmpty()) queryString += " where artistName like '%" + search + "%'";
        
        return getEntitiesByQuery(queryString);
    }
    
    /**
     * Get albums from the database
     * 
     * @param search    the string to search for in albumName  (empty string -> no search)
     * @param artistId  the artist id of the associated artist (0 -> all artists)              
     * @return          List of album entities, null if a database access error occurs
     */
    public List<Entity> getAlbums(String search, int artistId) {
        
        String queryString = "from Album";
        if (search != null && !search.isEmpty()) queryString += " where albumName like '%" + search + "%'";
        if (artistId > 0) {
            if (queryString.equals("from Album")) queryString += " where";
            else queryString += " and";
            queryString += " artist.artistId = " + artistId;
        }
        
        System.out.println(queryString);
        return getEntitiesByQuery(queryString);
    }
    
    /**
     * Get songs from the database
     * 
     * @param search    the string to search for in songName  (empty string -> no search)
     * @param albumId   the albumt id of the associated album (0 -> all albums)              
     * @return          List of song entities, null if a database access error occurs
     */
    public List<Entity> getSongs(String search, int albumId) {
        String queryString = "from Song";
        if (search != null && !search.isEmpty()) queryString += " where songName like '%" + search + "%'";
        if (albumId > 0) {
            if (queryString.equals("from Song")) queryString += " where";
            else queryString += " and";
            queryString += " album.albumId = " + albumId;
        }
        
        return getEntitiesByQuery(queryString);
    }
    

    // Add to the database //
    
    /**
     * Add an entity to the database
     * 
     * @param   entity entity to add
     */
    private void addEntity(Entity entity) {
        Session session = sessionFactory.openSession();
        
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Add an artist to the artists table
     * 
     * @param artistName    name of the artist
     * @return              the Artist object 
     */
    public Artist addArtist(String artistName) {
        Artist artist = new Artist(artistName);
        addEntity(artist);
        
        return artist;
    }
    
    /**
     * Add an album to the albums table
     * 
     * @param artist artist associated with the album
     * @param albumName name of the album
     */
    public Album addAlbum(Artist artist, String albumName) {
        Album album = artist.makeAlbum(albumName);
        addEntity(album);
        
        return album;
    }
    
    /**
     * Add a song to the songs table
     * 
     * @param album album associated with the song
     * @param songName name of the song
     */
    public Song addSong(Album album, String songName) {
        Song song = album.makeSong(songName);
        addEntity(song);
        
        return song;
    }

    
    // Delete from the database //
    
    /**
     * Delete an entity from the database by id
     * 
     * @param entityClass   class of the entity
     * @param entityId      id (primary key) of the entity
     * @return              true if the entity was successfully found and deleted, false otherwise
     */
    public boolean deleteEntityById(Class<? extends Entity> entityClass, int id) {
        Session session = sessionFactory.openSession();
        
        session.beginTransaction();
        Entity entity = (Entity) session.get(entityClass, id);
        if (entity == null) return false;
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
        
        return true;
    }
    
    
    // Bean utilities //
    
    public void afterPropertiesSet() throws Exception {
        Configuration conf = new Configuration();
        conf.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        sessionFactory = conf.buildSessionFactory(serviceRegistry);
    }

    public void destroy() throws Exception {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

}