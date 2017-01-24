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
        

    // Add to the database //
    private void addEntity(Entity entity) throws EntityServiceException {
        
    	Session session = null;
        try {
	        session = sessionFactory.openSession();
	        session.beginTransaction();
	        session.save(entity);
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