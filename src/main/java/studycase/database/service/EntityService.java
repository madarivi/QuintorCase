package studycase.database.service;

import java.util.List;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;

public interface EntityService {
	public abstract Entity getEntityById(Class<? extends Entity> entityClass, int entityId);
	public abstract List<Entity> getEntities(Class<? extends Entity> entityClass);
	public abstract boolean deleteEntityById(Class<? extends Entity> entityClass, int entityId);
	public abstract Artist addArtist(String artistName);
	public abstract Album addAlbum(Artist artist, String albumName);
	public abstract Song addSong(Album album, String songName);
}
