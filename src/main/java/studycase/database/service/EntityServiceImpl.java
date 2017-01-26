package studycase.database.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;

/**
 * @author Marius
 * 
 * Service accessing the database via Hibernate
 *
 */
@Service
public class EntityServiceImpl implements EntityService{
    
    @Autowired
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
        }
        catch (HibernateException e) {
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
        catch(HibernateException e) {
        	System.err.println(e);
        	if (session != null) session.getTransaction().rollback();
        	throw new EntityServiceException(e);
        }
        finally {
        	if (session != null) session.close();
        }
        
        return entities;
    }
    
    // Add to the database //
    private void addEntity(Entity entity) throws EntityServiceException {
        
    	Session session = null;
        try {
	        session = sessionFactory.openSession();
	        session.beginTransaction();
	        session.save(entity);
	        session.getTransaction().commit();
        }
        catch(HibernateException e) {
        	System.err.println(e);
        	if (session != null) session.getTransaction().rollback();
        	throw new EntityServiceException(e);
        }
        finally {
        	if (session != null) session.close();
        }
    }
    
    @Override
    public Artist addArtist(String artistName) throws EntityServiceException {
        Artist artist = new Artist(artistName);
        addEntity(artist);
        
        return artist;
    }
    
    @Override
    public Album addAlbum(Artist artist, String albumName) throws EntityServiceException {
        Album album = artist.makeAlbum(albumName);
        addEntity(album);
        
        return album;
    }
    
    @Override
    public Song addSong(Album album, String songName) throws EntityServiceException {
        Song song = album.makeSong(songName);
        addEntity(song);
        
        return song;
    }

    // Delete from the database //
    public boolean deleteEntityById(Class<? extends Entity> entityClass, int id) throws EntityServiceException {
        boolean deleted = false;
    	
    	Session session = null;
    	
    	try {
    		session = sessionFactory.openSession();
	        session.beginTransaction();
	        Entity entity = (Entity) session.get(entityClass, id);
	        if (entity != null) {
	        	session.delete(entity);
	        	deleted = true;
	        }
	        session.getTransaction().commit();
    	}
        catch(HibernateException e) {
        	System.err.println(e);
        	if (session != null) session.getTransaction().rollback();
        	throw new EntityServiceException(e);
        }
        finally {
        	if (session != null) session.close();
        }
        
	    return deleted;
    }
    
}