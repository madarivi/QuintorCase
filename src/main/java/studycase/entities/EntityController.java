/**
 * 
 */
package studycase.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Marius
 * 
 * Controller for hibernate entities
 *
 */
public class EntityController {
    
    private final ServiceRegistry serviceRegistry;
    private final SessionFactory sessionFactory;
    
    /**
     * Default constructor sets up a sessionFactory;
     */
    public EntityController() {
        Configuration conf = new Configuration();
        conf.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        sessionFactory = conf.buildSessionFactory(serviceRegistry);
        System.out.println("configuration and hbm files loaded succesfully");
    }
    
    // get from the database //
    
    /**
     * Get an entity from the database
     * @param entityClass class of the entity to retrieve
     * @param entityId id (primary key) of the entity to retrieve
     * @return the retrieved entity if found in the database, null otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T getEntityById(Class<T> entityClass, int entityId) {
        System.out.println("start of getEntityById");
        Session session = sessionFactory.openSession();
        T entity = null;
        
        try{
            session.beginTransaction();
            entity = (T) session.get(entityClass, entityId);
            session.getTransaction().commit();
            session.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }
        return entity;
    }
    
    
    // Add to the database //
    
    /**
     * Add an entity to the database
     * 
     * @param entity entity to add
     * @return true if the entity is succesfully added, false otherwise
     */
    private boolean addEntity(Entity entity) {
        Session session = sessionFactory.openSession();
        
        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            session.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            session.close();
        }
        
        return true;
    }
    
    /**
     * Add an artist to the artists table
     * 
     * @param artistName name of the artist
     * @return the artist if succesfully added, null otherwise
     */
    public Artist addArtist(String artistName) {
        Artist artist = new Artist(artistName);
        boolean added = addEntity(artist);
        
        if (added) return artist;
        return null;
    }
    
    /**
     * Add an album to the albums table
     * 
     * @param artist artist associated with the album
     * @param albumName name of the album
     */
    public Album addAlbum(Artist artist, String albumName) {
        Album album = artist.makeAlbum(albumName);
        boolean added = addEntity(album);
        
        if (added) return album;
        return null;
    }
    
    /**
     * Add a song to the songs table
     * 
     * @param album album associated with the song
     * @param songName name of the song
     */
    public Song addSong(Album album, String songName) {
        Song song = album.makeSong(songName);
        boolean added = addEntity(song);
        
        if (added) return song;
        return null;
    }

    
    public void close() {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

}
