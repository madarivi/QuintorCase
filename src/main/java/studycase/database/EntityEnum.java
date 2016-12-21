package studycase.database;

import studycase.database.entities.Album;
import studycase.database.entities.Artist;
import studycase.database.entities.Entity;
import studycase.database.entities.Song;

public enum EntityEnum {
    ARTISTS(Artist.class),
    ALBUMS(Album.class),
    SONGS(Song.class);

    private Class<? extends Entity> cls;

    EntityEnum(Class<? extends Entity> cls) {
      this.cls = cls;
    }

    public Class<? extends Entity> getEntityClass() {
      return this.cls;
    }

    public static EntityEnum fromString(String text) {
        if (text != null) {
            System.out.println(text);
            for (EntityEnum e : EntityEnum.values()) {
                if (text.equals(e.toString().toLowerCase())) return e;
            }
        }
        return null;
    }
  }
