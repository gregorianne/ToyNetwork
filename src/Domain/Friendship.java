package Domain;

/**
 * Reprezintă o relație de prietenie între doi utilizatori ai rețelei.
 * Prietenia este simetrică: (A, B) este aceeași relație cu (B, A).
 *
 * @author Andra
 * @version 1.0
 */
public class Friendship extends Entity {
    /** Id-ul primului utilizator din prietenie. */
    private int id_user1;

    /** Id-ul celui de-al doilea utilizator din prietenie. */
    private int id_user2;

    /**
     * Creează o prietenie între doi utilizatori.
     *
     * @param id       id-ul unic al prieteniei
     * @param id_user1 id-ul primului utilizator
     * @param id_user2 id-ul celui de-al doilea utilizator
     */
    public Friendship(int id, int id_user1, int id_user2) {
        super(id);
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
    }

    /**
     * Returnează id-ul primului utilizator.
     *
     * @return id-ul primului utilizator
     */
    public int getId_user1() {
        return id_user1;
    }

    /**
     * Returnează id-ul celui de-al doilea utilizator.
     *
     * @return id-ul celui de-al doilea utilizator
     */
    public int getId_user2() {
        return id_user2;
    }

    /**
     * Setează id-ul primului utilizator.
     *
     * @param id_user1 noul id al primului utilizator
     */
    public void setId_user1(int id_user1) {
        this.id_user1 = id_user1;
    }

    /**
     * Setează id-ul celui de-al doilea utilizator.
     *
     * @param id_user2 noul id al celui de-al doilea utilizator
     */
    public void setId_user2(int id_user2) {
        this.id_user2 = id_user2;
    }
}