/**
 * 
 */
package studycase.entities;

import java.util.List;

import org.hibernate.Query;
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
    
    private static EntityController instance;
    public static EntityController getInstance() {
        if (instance == null) instance = new EntityController();
        return instance;
    }
    
    private final ServiceRegistry serviceRegistry;
    private final SessionFactory sessionFactory;
    
    /**
     * constructor sets up a sessionFactory;
     */
    private EntityController() {
        Configuration conf = new Configuration();
        conf.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        sessionFactory = conf.buildSessionFactory(serviceRegistry);
        System.out.println("configuration and hbm files loaded succesfully");
    }
    
    // get from the database //
    
    /**
     * Get an entity from the database by id
     * 
     * @param entityClass   class of the entity to retrieve
     * @paramentityId       id (primary key) of the entity to retrieve
     * @return              the retrieved entity if found in the database, null otherwise
     */
    public Entity getEntityById(Class<?> entityClass, int entityId) {
        Session session = sessionFactory.openSession();
        Entity entity = null;
        
        try{
            session.beginTransaction();
            entity = (Entity) session.get(entityClass, entityId);
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
