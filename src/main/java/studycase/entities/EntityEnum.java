package studycase.entities;

public enum EntityEnum {
    ARTIST(Artist.class),
    ALBUM(Album.class),
    SONG(Song.class);

    private Class<? extends Entity> cls;

    EntityEnum(Class<? extends Entity> cls) {
      this.cls = cls;
    }

    public Class<? extends Entity> getEntityClass() {
      return this.cls;
    }

    public static EntityEnum fromString(String text) {
        if (text != null) {
            for (EntityEnum e : EntityEnum.values()) {
                if (text.equalsIgnoreCase(e.cls.getSimpleName())) return e;
            }
        }
        return null;
    }
  }

