package studycase.database.entities;

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
