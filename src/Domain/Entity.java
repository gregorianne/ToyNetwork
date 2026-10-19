package Domain;

/**
 * Clasă de bază pentru toate entitățile aplicației (User, Friendship etc.).
 * Gestioneaza identificatorul unic, care le este comun
 */
public class Entity {
    /** Codul unic al entitatii. */
    protected int id;

    /**
     * Creează o entitate cu id-ul dat.
     *
     * @param id codul unic al entitatii
     */
    public Entity(int id) {
        this.id = id;
    }

    /**
     * Returnează id-ul entitatii.
     *
     * @return codul unic al entității
     */
    public int getId() {
        return id;
    }

    /**
     * Setează id-ul entitatii
     *
     * @param id noul cod unic
     */
    public void setId(int id) {
        this.id = id;
    }
}