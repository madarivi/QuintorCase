package com.quintor.study_case.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;

public class EntityOps {
    private static final SessionFactory sessionFactory;
    private static final ServiceRegistry serviceRegistry;
    public static void main(String[] args) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            
            System.out.println("transaction started");
            Artist artist = new Artist("Jan Smit");
            System.out.println("Artist created");
            session.save(artist);
            System.out.println("Artist saved");
            
            Album album = artist.makeAlbum("Beste van Jantje");
            System.out.println("album created");
            session.save(album);
            System.out.println("album saved");
            
            Song song = album.makeSong("Kom dichterbij me");
            System.out.println("Song created");
            session.save(song);
            System.out.println("Song saved");
            
            session.getTransaction().commit();
            System.out.println("transaction commited");
            session.close();
            System.out.println("Session closed");
        } finally {
            sessionFactory.close();
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }

    static {
        Configuration conf = new Configuration();
        conf.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        sessionFactory = conf.buildSessionFactory(serviceRegistry);
        System.out.println("configuration and hbm files loaded succesfully");
    }
}
